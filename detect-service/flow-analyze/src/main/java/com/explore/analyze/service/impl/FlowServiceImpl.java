package com.explore.analyze.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.analyze.form.FlowQuery;
import com.explore.analyze.mappers.FlowMapper;
import com.explore.analyze.service.IFlowHourService;
import com.explore.analyze.service.IFlowService;
import com.explore.common.database.Flow;
import com.explore.common.database.FlowHour;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @ClassName FlowServiceImpl
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/15 16:43
 * @Version 1.0
 **/
@Service
public class FlowServiceImpl extends ServiceImpl<FlowMapper, Flow> implements IFlowService {

    private final IFlowHourService flowHourService;

    public FlowServiceImpl(IFlowHourService flowHourService) {
        this.flowHourService = flowHourService;
    }

    @Override
    public List<FlowHour> getFlowByQuery(FlowQuery query) {
        LambdaQueryWrapper<FlowHour> queryWrapper = new LambdaQueryWrapper<>();
        if (query.getCameraId() != null && query.getCameraId() > 0) {
            queryWrapper.eq(FlowHour::getCameraId, query.getCameraId());
        }

        LocalDate beginDate = null;
        LocalDate endDate = LocalDate.now();
        // todo 优化人流量统计方式
        if (FlowQuery.DAY.equals(query.getType())) {                // 天
            // 按小时统计人流量
            query.setBeginTime(query.getEndTime().minusDays(query.getNum()));
            beginDate = query.getBeginTime().toLocalDate();
            endDate = LocalDate.now();
            queryWrapper.ge(FlowHour::getDate, beginDate);
            queryWrapper.le(FlowHour::getDate, endDate);
            return flowHourService.getBaseMapper().selectList(queryWrapper);
        } else if (FlowQuery.WEEK.equals(query.getType())) {        // 周
            // 按天统计人流量
            query.setBeginTime(query.getEndTime().minusWeeks(query.getNum()));
            beginDate = query.getBeginTime().toLocalDate();
            endDate = LocalDate.now();
            return flowHourService.selectAllDayFlow(query.getCameraId(), beginDate, endDate);
        } else if (FlowQuery.MONTH.equals(query.getType())) {       // 月
            // 按天统计人流量
            query.setBeginTime(query.getEndTime().minusMonths(query.getNum()));
            beginDate = query.getBeginTime().toLocalDate();
            endDate = LocalDate.now();
            return flowHourService.selectAllDayFlow(query.getCameraId(), beginDate, endDate);
        }

        return null;
    }
}
