package com.explore.analyze.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.analyze.form.FlowQuery;
import com.explore.analyze.mappers.FlowMapper;
import com.explore.analyze.service.IFlowService;
import com.explore.common.database.Flow;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName FlowServiceImpl
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1516:43
 * @Version 1.0
 **/
@Service
public class FlowServiceImpl extends ServiceImpl<FlowMapper, Flow> implements IFlowService {
    @Override
    public List<Flow> getFlowByQuery(FlowQuery query) {
        LambdaQueryWrapper<Flow> queryWrapper = new LambdaQueryWrapper<>();
        if (query.getCameraId() != null && query.getCameraId() > 0) {
            queryWrapper.eq(Flow::getCameraId, query.getCameraId());
        }
        if (FlowQuery.DAY.equals(query.getType())) {                // 天
            query.setBeginTime(query.getEndTime().minusDays(query.getNum()));
        } else if (FlowQuery.WEEK.equals(query.getType())) {        // 周
            query.setBeginTime(query.getEndTime().minusWeeks(query.getNum()));
        } else if (FlowQuery.MONTH.equals(query.getType())) {       // 月
            query.setBeginTime(query.getEndTime().minusMonths(query.getNum()));
        }
        queryWrapper.ge(Flow::getCurrentTime, query.getBeginTime())
                .le(Flow::getCurrentTime, query.getEndTime());
        // todo 人流量统计方式
        return null;
//        return page(new Page<>(1), queryWrapper);
    }
}
