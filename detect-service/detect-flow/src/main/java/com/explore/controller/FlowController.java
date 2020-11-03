package com.explore.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.explore.common.ServerResponse;
import com.explore.common.database.Flow;
import com.explore.servcie.IFlowService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
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


    public FlowController(IFlowService flowService) {
        this.flowService = flowService;
    }
    /**
     * 保存检测数据
     *
     * @param flow obj
     */
    @PostMapping
    public void save(@RequestBody Flow flow) {
        flowService.saveData(flow);
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
}
