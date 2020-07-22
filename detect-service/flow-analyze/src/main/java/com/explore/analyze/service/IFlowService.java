package com.explore.analyze.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.explore.analyze.form.FlowQuery;
import com.explore.common.database.Flow;
import com.explore.common.database.FlowHour;

import java.util.List;

/**
 * @InterfaceName IFlowService
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1516:46
 * @Version 1.0
 **/
public interface IFlowService extends IService<Flow> {

    List<FlowHour> getPeriodFlowByQuery(FlowQuery query);

    List<FlowHour> getFlowByQuery(FlowQuery query);
}
