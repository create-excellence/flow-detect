package com.explore.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.explore.client.CameraClient;
import com.explore.common.ServerResponse;
import com.explore.common.database.Camera;
import com.explore.common.database.Flow;
import com.explore.servcie.IFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author PinTeh
 * @date 2020/6/22
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/flow")
public class FlowController {

    private final IFlowService flowService;

    private final CameraClient  cameraClient;

    public FlowController(IFlowService flowService, CameraClient cameraClient) {
        this.flowService = flowService;
        this.cameraClient = cameraClient;
    }

    /**
     * 保存检测数据
     * @param flow obj
     */
    @PostMapping
    public void save(@RequestBody Flow flow){
        flowService.save(flow);
        Camera camera = cameraClient.getById(flow.getCameraId());
        if (flow.getFlow().compareTo(camera.getWarning())>0){
            //TODO
            log.info(" warning => flow value : {}",flow.getFlow());
        }
    }

    /**
     * 获取最新记录
     * @return ServerResponse
     */
    @GetMapping
    public ServerResponse get(){
        Flow flow = flowService.getOne(new QueryWrapper<Flow>().orderByDesc("id").last("limit 0,1"));
        return ServerResponse.createBySuccess(flow);
    }

    /**
     * 根据摄像头获取检测数据
     * @param cid 摄像头id
     * @return ServerResponse
     */
    @GetMapping("/{cid}")
    public ServerResponse getListByCid(@PathVariable("cid") Long cid,
                                       @RequestParam(required = false,defaultValue = "1")Integer page,
                                       @RequestParam(required = false,defaultValue = "10")Integer limit){
        return ServerResponse.createBySuccess(
                flowService.page(
                        new Page<>(page,limit),
                        new QueryWrapper<Flow>().eq("camera_id",cid).orderByDesc("id")));
    }
}
