package com.explore.servcie.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.client.CameraClient;
import com.explore.client.SnapshotClient;
import com.explore.common.Const;
import com.explore.common.ServerResponse;
import com.explore.common.database.Camera;
import com.explore.common.database.Flow;
import com.explore.common.database.Snapshot;
import com.explore.common.database.Warning;
import com.explore.mappers.FlowMapper;
import com.explore.servcie.IFlowService;
import com.explore.servcie.IWarningService;
import com.explore.socket.WebMessage;
import com.explore.socket.WebSocketServer;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author PinTeh
 * @date 2020/6/22
 */
@Slf4j
@Service
public class FlowServiceImpl extends ServiceImpl<FlowMapper, Flow> implements IFlowService {

    @Value("${detect.snapshot.upload-path}")
    private String snapshotUploadPath;

    private static Cache<String, String> cache;
    private static Cache<String, String> cameraCache;

    static {
        cache = CacheBuilder.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .concurrencyLevel(10)
                .recordStats()
                .build();

        cameraCache = CacheBuilder.newBuilder()
                .maximumSize(2000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .concurrencyLevel(10)
                .recordStats()
                .build();
    }

    private final WebSocketServer webSocketServer;

    private final CameraClient cameraClient;

    private final IWarningService warningService;

    private final SnapshotClient snapshotClient;


    public FlowServiceImpl(CameraClient cameraClient, IWarningService warningService, WebSocketServer webSocketServer,SnapshotClient snapshotClient) {
        this.cameraClient = cameraClient;
        this.warningService = warningService;
        this.webSocketServer = webSocketServer;
        this.snapshotClient = snapshotClient;
    }

    @Override
    public void saveData(Flow flow, MultipartFile file) {
        flow.setRecordTime(LocalDateTime.now());
        flow.setCreateTime(LocalDateTime.now());
        flow.setDetectStatus(0);
        this.baseMapper.insert(flow);
        Camera camera;
        String cameraString = cameraCache.getIfPresent(String.valueOf(flow.getCameraId()));
        if (cameraString == null){
            camera = cameraClient.getById(flow.getCameraId()).getData();
            if (camera == null || camera.getId().equals(-1)){
                log.info("返回Camera为无效，id={}",flow.getCameraId());
                return;
            }
            // 保存camera到缓存
            cameraCache.put(String.valueOf(flow.getCameraId()), JSON.toJSONString(camera));
            log.info("获取camera,保存到cache");
        }else{
            camera = JSON.parseObject(cameraString,Camera.class);
            log.info("获取camera,从cache中获取");
        }

        String token = WebSocketServer.tokenMAP.get(camera.getUserId().toString());
        if(token!=null){
            webSocketServer.sendMessageToAll(token
                    , WebMessage.createFlowMessage(String.valueOf(flow.getFlow()),LocalDateTime.now(),camera.getId()).toJson());
        }

        // 保存图片到服务器
        String fileName = saveSnapshotsFile(file, camera.getId());
        String filePath = "/" + camera.getId() + "/" + fileName;

        if (flow.getFlow().compareTo(camera.getWarning().longValue()) > 0) {
            String value = cache.getIfPresent(String.valueOf(camera.getId()));
            if (value != null) {
                return;
            }
            Snapshot snapshot =new Snapshot();
            snapshot.setCameraId(Long.valueOf(camera.getId()));
            snapshot.setInfo("人流量超出预警值");
            snapshot.setFileName(fileName);
            snapshot.setPath(filePath);
            // 1代表预警
            snapshot.setStatus(1);
            snapshotClient.add(snapshot);
            LocalDateTime now = LocalDateTime.now();
            cache.put(String.valueOf(camera.getId()), now.toString());
            warningService.save(Warning.builder().number(flow.getFlow().intValue()).warning(camera.getWarning()).createTime(now).build());
            log.info(" 异常人流量 : warning => flow value : {}", flow.getFlow());
        }
        boolean isCover =new Random().nextInt(10) % 100 == 0;
        if(isCover){
            camera.setCover(filePath);
            cameraClient.reviseCamera(camera,camera.getId());
        }

    }

    @Override
    public ServerResponse getUserCameraAndFlow() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(Const.TOKEN);
        Integer userId = webSocketServer.getUserId(token);
        ServerResponse serverResponse = cameraClient.getByUserId(userId);
        if(serverResponse.isSuccess()){
            if(serverResponse.getData()!=null){
                List<Map> result = new ArrayList<>();
                List<LinkedHashMap> cameraList =(List<LinkedHashMap>) ((LinkedHashMap)serverResponse.getData()).get("records");
                for(LinkedHashMap o:cameraList){
                    Map map = new HashMap();
                    map.put("camera",o);
                    Flow flow = this.getOne(new QueryWrapper<Flow>().eq("camera_id", (Integer) o.get("id")).orderByDesc("id").last("limit 0,1"));
                    Long num = 0L;
                    if(flow != null) num = flow.getFlow();
                    map.put("flow",num);
                    result.add(map);
                }
                return ServerResponse.createBySuccess(result);
            }

        }
        return null;
    }

    private String saveSnapshotsFile(MultipartFile file,Integer cameraId){
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)){
            log.error("get file original filename is empty");
            return null;
        }

        String ext = originalFilename.substring(originalFilename.indexOf("."));
        String timeFileNamePrefix = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(new Date());
        String newFileName = timeFileNamePrefix + ext;

        String dir = snapshotUploadPath + "/" + cameraId + "/";
        File fileDir = new File(dir);
        if (!fileDir.exists()){
            boolean mkdirs = fileDir.mkdirs();
            log.info("create file dir : result = {}.", mkdirs);
        }
        File localFile = new File(dir + newFileName);
        try {
            file.transferTo(localFile);
        } catch (IOException e) {
            log.error("save file error",e);
        }

        return newFileName;
    }
}
