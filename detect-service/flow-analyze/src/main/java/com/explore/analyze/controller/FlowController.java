package com.explore.analyze.controller;

import com.explore.analyze.form.FlowQuery;
import com.explore.analyze.service.IFlowService;
import com.explore.common.ServerResponse;
import com.explore.common.database.FlowHour;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FlowController
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1516:42
 * @Version 1.0
 **/
@RestController
@RequestMapping("/analyze")
public class FlowController {

    private final IFlowService flowService;

    public FlowController(IFlowService flowService) {
        this.flowService = flowService;
    }

    /**
     * @Author 安羽兮
     * @Description 获取指定设备指定时间段内历史人流量
     * @Date 17:51 2020/7/15
     * @Param [query]
     * @Return com.explore.common.ServerResponse
     **/
    @GetMapping("/flow")
    public ServerResponse getFlow(FlowQuery query) {
        List<FlowHour> flows = flowService.getPeriodFlowByQuery(query);
        if (null != flows)
            return ServerResponse.createBySuccess(flows);
        return ServerResponse.createByError();
    }

    // todo 分析人流量功能
}