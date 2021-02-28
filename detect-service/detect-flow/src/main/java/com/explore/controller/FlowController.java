package com.explore.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.explore.client.CameraClient;
import com.explore.client.UserClient;
import com.explore.common.ServerResponse;
import com.explore.common.database.Flow;
import com.explore.servcie.IFlowService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author PinTeh
 * @date 2020/6/22
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/flow")
public class FlowController {

    private final IFlowService flowService;

    private final UserClient userClient;

    private final CameraClient cameraClient;


    public FlowController(IFlowService flowService,UserClient userClient,CameraClient cameraClient) {
        this.flowService = flowService;
        this.userClient = userClient;
        this.cameraClient = cameraClient;
    }
    /**
     * 保存检测数据
     *
     * @param flow obj
     */
    @PostMapping
    public void save(Flow flow,@RequestParam(value="file", required=false) MultipartFile file) {
        flowService.saveData(flow,file);
    }

    /**
     * 获取最新记录
     *
     * @return ServerResponse
     */
    @GetMapping
    public ServerResponse<Flow> get(@RequestParam("cid") Integer cid) {
        Flow flow = flowService.getOne(new QueryWrapper<Flow>().eq("camera_id", cid).orderByDesc("id").last("limit 0,1"));
        return ServerResponse.createBySuccess(flow);
    }

    /**
     * 获取最新集合记录
     *
     * @return ServerResponse
     */
    @GetMapping("/ids")
    public ServerResponse getList(@RequestParam("ids") String ids) {
        String[] strings =ids.split(",");
        List<Flow> res = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            Flow flow = flowService.getOne(new QueryWrapper<Flow>().eq("camera_id", strings[i]).orderByDesc("id").last("limit 0,1"));
            res.add(flow);
        }
        return ServerResponse.createBySuccess(res);
    }

    /**
     * 根据摄像头获取检测数据
     *
     * @param cid 摄像头id
     * @return ServerResponse
     */
    @GetMapping("/list")
    public ServerResponse getListByCid(@RequestParam("cid") Integer cid,
                                       @RequestParam(required = false, defaultValue = "1") Integer page,
                                       @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return ServerResponse.createBySuccess(
                flowService.page(
                        new Page<>(page, limit),
                        new QueryWrapper<Flow>().eq("camera_id", cid).orderByDesc("id")));
    }


    @GetMapping("/getUserCameraAndFlow")
    public ServerResponse getUserCameraAndFlow() {
        return   flowService.getUserCameraAndFlow();
    }
}
