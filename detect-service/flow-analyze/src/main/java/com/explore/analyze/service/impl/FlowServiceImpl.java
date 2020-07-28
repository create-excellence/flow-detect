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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
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

    /**
     * @Author 安羽兮
     * @Description 返回指定日期内每小时人流量
     * @Date 15:48 2020/7/22
     * @Param [query]
     * @Return java.util.List<com.explore.common.database.FlowHour>
     **/
    @Override
    public List<FlowHour> getPeriodFlowByQuery(FlowQuery query) {
        LambdaQueryWrapper<FlowHour> queryWrapper = new LambdaQueryWrapper<>();
        if (query.getCameraId() != null && query.getCameraId() > 0) {
            queryWrapper.eq(FlowHour::getCameraId, query.getCameraId());
        } else {
            return null;
        }
        LocalDate beginDate = null;
        LocalDate endDate = query.getEndTime().toLocalDate();
        // 默认一周内人流量
        if (null == query.getBeginTime()) {
            query.setBeginTime(query.getEndTime().minusWeeks(query.getNum()));
        }
        beginDate = query.getBeginTime().toLocalDate();
        // 按小时统计人流量
        queryWrapper.ge(FlowHour::getDate, beginDate);
        queryWrapper.le(FlowHour::getDate, endDate);
        // 按照日期, 小时排序
        queryWrapper.orderByAsc(FlowHour::getDate).orderByAsc(FlowHour::getHour);
        List<FlowHour> data = flowHourService.getBaseMapper().selectList(queryWrapper);
        return packageData(query, data);
    }

    @Override
    public List<Flow> getAllFlowByQuery(FlowQuery query) {
        LambdaQueryWrapper<Flow> queryWrapper = new LambdaQueryWrapper<>();
        if (query.getCameraId() != null && query.getCameraId() > 0) {
            queryWrapper.eq(Flow::getCameraId, query.getCameraId());
        } else {
            return null;
        }
        LocalDateTime beginDate = null;
        LocalDateTime endDate = query.getEndTime();
        // 默认一周内人流量
        if (null == query.getBeginTime()) {
            query.setBeginTime(query.getEndTime().minusWeeks(query.getNum()));
        }
        beginDate = query.getBeginTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // LocalDateTime使用apply查询
        // todo 加上这句无法查询到数据？？？
//        queryWrapper.apply("UNIX_TIMESTAMP(current_time) <= UNIX_TIMESTAMP('" + formatter.format(endDate) + "')");
        queryWrapper.apply("UNIX_TIMESTAMP(current_time) >= UNIX_TIMESTAMP('" + formatter.format(beginDate) + "')");
        // 错误写法
//        queryWrapper.le(Flow::getCurrentTime, endDate.toLocalDate());
//        queryWrapper.ge(Flow::getCurrentTime, beginDate.toLocalDate());
        // 按照日期排序
        queryWrapper.orderByAsc(Flow::getRecordTime);
        List<Flow> data = this.baseMapper.selectList(queryWrapper);
        return data;
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

    /**
     * @Author 安羽兮
     * @Description 填充空白人流量
     * @Date 15:48 2020/7/22
     * @Param [query, data]
     * @Return java.util.List<com.explore.common.database.FlowHour>
     **/
    private List<FlowHour> packageData(FlowQuery query, List<FlowHour> data) {
        FlowHour flowHour = null;
        LocalDate beginDate = query.getBeginTime().toLocalDate();
        LocalDate endDate = query.getEndTime().toLocalDate();
        List<FlowHour> result = new LinkedList<>();
        while (beginDate.compareTo(endDate) <= 0) {
            // 一天24小时
            for (int hour = 0; hour < 24; hour++) {
                // 查询到的数据是按照日期, 小时排序
                if (!data.isEmpty() && data.get(0).getDate().equals(beginDate) && data.get(0).getHour() == hour) {
                    // 数据库中查询到了对应的FlowHour
                    result.add(data.get(0));
                    data.remove(0);
                } else {
                    flowHour = new FlowHour();
                    flowHour.setDate(beginDate);
                    flowHour.setHour(hour);
                    flowHour.setCameraId(query.getCameraId());
                    flowHour.setHourFlow(0);        // 找不到对应时间段的人流量，则设人流量为0
                    result.add(flowHour);
                }
            }
            beginDate = beginDate.plusDays(1);      // 向前推移一天
        }
        return result;
    }
}
