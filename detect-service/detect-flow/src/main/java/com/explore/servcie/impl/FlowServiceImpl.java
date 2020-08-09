package com.explore.servcie.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.client.CameraClient;
import com.explore.common.database.Camera;
import com.explore.common.database.Flow;
import com.explore.common.database.Warning;
import com.explore.mappers.FlowMapper;
import com.explore.servcie.IFlowService;
import com.explore.servcie.IWarningService;
import com.explore.socket.WebMessage;
import com.explore.socket.WebSocketServer;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author PinTeh
 * @date 2020/6/22
 */
@Slf4j
@Service
public class FlowServiceImpl extends ServiceImpl<FlowMapper, Flow> implements IFlowService {

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

    private final FlowMapper flowMapper;

    private final CameraClient cameraClient;

    private final IWarningService warningService;

    private final WebSocketServer webSocketServer;

    public FlowServiceImpl(CameraClient cameraClient, IWarningService warningService, WebSocketServer webSocketServer, FlowMapper flowMapper) {
        this.cameraClient = cameraClient;
        this.warningService = warningService;
        this.webSocketServer = webSocketServer;
        this.flowMapper = flowMapper;
    }

    @Override
    public void saveData(Flow flow) {
        flowMapper.insert(flow);
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
        if (flow.getFlow().compareTo(camera.getWarning().longValue()) > 0) {
            String value = cache.getIfPresent(String.valueOf(camera.getId()));
            if (value != null) {
                return;
            }
            LocalDateTime now = LocalDateTime.now();
            cache.put(String.valueOf(camera.getId()), now.toString());
            warningService.save(Warning.builder().number(flow.getFlow().intValue()).warning(camera.getWarning()).createTime(now).build());
            log.info(" 异常人流量 : warning => flow value : {}", flow.getFlow());
        }
        webSocketServer.sendMessageToAll(String.valueOf(flow.getCameraId())
                , WebMessage.createFlowMessage(String.valueOf(flow.getCameraId()),LocalDateTime.now()).toJson());
    }
}
