package com.explore.analyze.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.analyze.client.CameraClient;
import com.explore.analyze.form.SnapshotQuery;
import com.explore.analyze.mappers.SnapshotMapper;
import com.explore.analyze.service.IFlowService;
import com.explore.analyze.service.ISnapshotService;
import com.explore.analyze.vo.SnapshotVo;
import com.explore.common.ServerResponse;
import com.explore.common.database.Flow;
import com.explore.common.database.Snapshot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName SnapshotServiceImpl
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1518:07
 * @Version 1.0
 **/
@Slf4j
@Service
public class SnapshotServiceImpl extends ServiceImpl<SnapshotMapper, Snapshot> implements ISnapshotService {

    @Value("${spring.uploadPath}")
    private String uploadPath;

    @Value("${detect.snapshot.upload-path}")
    private String snapshotUploadPath;

    private final ResourceLoader resourceLoader;

    private final IFlowService flowService;

    private final CameraClient cameraClient;


    public SnapshotServiceImpl(ResourceLoader resourceLoader, IFlowService flowService, CameraClient cameraClient) {
        this.resourceLoader = resourceLoader;
        this.flowService = flowService;
        this.cameraClient = cameraClient;

    }


    @Override
    public Page<SnapshotVo> pageByQuery(SnapshotQuery query) {
        Page page = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<Snapshot> queryWrapper = new LambdaQueryWrapper();
        Page data = page(page, queryWrapper);
        List<SnapshotVo> snapshotVos = new ArrayList<>();
        data.getRecords().stream().forEach(snapshot -> {
            SnapshotVo snapshotVo = new SnapshotVo();
            BeanUtils.copyProperties(snapshot, snapshotVo);
            Flow flow = flowService.getById(snapshotVo.getFlowId());
            snapshotVo.setFlow(flow);
            if (null != flow)
                snapshotVo.setCamera(cameraClient.getById(flow.getCameraId()).getData());
            // 满足筛选条件才加入集合
            if (null == query.getFlow() || flow.getFlow().intValue() >= query.getFlow().intValue()) {
                if (null == query.getOrganizationId() || snapshotVo.getCamera().getOrganizationId().equals(query.getOrganizationId()))
                    snapshotVos.add(snapshotVo);
            } else {
                snapshotVo = null;
            }
        });
        data.setRecords(snapshotVos);
        return data;
    }

    @Override
    public ServerResponse upload(MultipartFile file) {
        // 图片路径
        String imgUrl = null;
        //上传
        try {
            String filename = upload(file, uploadPath, file.getOriginalFilename());
            Map result = new HashMap<>();
            result.put("path", filename);
            return ServerResponse.createBySuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("上传失败");
        }
    }

    @Override
    public ResponseEntity show(String fileName) {
        try {
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            return ResponseEntity.ok(resourceLoader.getResource("file:" + uploadPath + fileName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public void saveV2(MultipartFile file, Long cameraId, Long flowCount) {
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)){
            log.error("get file original filename is empty");
            return;
        }

        String ext = originalFilename.substring(originalFilename.indexOf("."));
        String timeFileNamePrefix = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(new Date());
        String newFileName = timeFileNamePrefix + ext;

        String dir = uploadPath + "/" + cameraId + "/";
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

        Snapshot snapshot = new Snapshot();
        snapshot.setCameraId(cameraId);
        snapshot.setFlowCount(flowCount);
        snapshot.setFileName(originalFilename);
        this.baseMapper.insert(snapshot);
    }

    private String upload(MultipartFile file, String path, String fileName) throws Exception {
        // 生成新的文件名
        String realPath = path + "/" + UUID.randomUUID().toString().replace("-", "") + fileName.substring(fileName.lastIndexOf("."));
        File dest = new File(realPath);
        // 判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        // 保存文件
        file.transferTo(dest);
        return dest.getName();
    }
}
