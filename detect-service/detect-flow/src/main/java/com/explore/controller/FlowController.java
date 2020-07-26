package com.explore.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.explore.client.CameraClient;
import com.explore.common.ServerResponse;
import com.explore.common.database.Camera;
import com.explore.common.database.Flow;
import com.explore.common.database.Warning;
import com.explore.servcie.IFlowService;
import com.explore.servcie.IWarningService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author PinTeh
 * @date 2020/6/22
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/flow")
public class FlowController {

    private static Cache<String, String> cache;

    static {
        cache = CacheBuilder.newBuilder()
                .maximumSize(200)
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .concurrencyLevel(10)
                .recordStats()
                .build();
    }

    private final IFlowService flowService;

    private final CameraClient  cameraClient;

    private final IWarningService warningService;

    public FlowController(IFlowService flowService, CameraClient cameraClient, IWarningService warningService) {
        this.flowService = flowService;
        this.cameraClient = cameraClient;
        this.warningService = warningService;
    }

    /**
     * 保存检测数据
     * @param flow obj
     */
    @PostMapping
    public void save(@RequestBody Flow flow){
        flowService.save(flow);
        Camera camera = cameraClient.getById(flow.getCameraId()).getData();
        if (flow.getFlow().compareTo(camera.getWarning())>0){
            String value = cache.getIfPresent(String.valueOf(camera.getId()));
            if (value != null){
                return;
            }
            LocalDateTime now = LocalDateTime.now();
            cache.put(String.valueOf(camera.getId()), now.toString());
            warningService.save(Warning.builder().number(flow.getFlow()).warning(camera.getWarning()).createTime(now).build());
            log.info(" warning => flow value : {}",flow.getFlow());
        }
    }

    /**
     * 获取最新记录
     * @return ServerResponse
     */
    @GetMapping
    public ServerResponse get(@RequestParam("cid")Integer cid){
        Flow flow = flowService.getOne(new QueryWrapper<Flow>().eq("camera_id",cid).orderByDesc("id").last("limit 0,1"));
        return ServerResponse.createBySuccess(flow);
    }

    /**
     * 根据摄像头获取检测数据
     * @param cid 摄像头id
     * @return ServerResponse
     */
    @GetMapping("/list")
    public ServerResponse getListByCid(@RequestParam("cid") Integer cid,
                                       @RequestParam(required = false,defaultValue = "1")Integer page,
                                       @RequestParam(required = false,defaultValue = "10")Integer limit){
        return ServerResponse.createBySuccess(
                flowService.page(
                        new Page<>(page,limit),
                        new QueryWrapper<Flow>().eq("camera_id",cid).orderByDesc("id")));
    }
}
