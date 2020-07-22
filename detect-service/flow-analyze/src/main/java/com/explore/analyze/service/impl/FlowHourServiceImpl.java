package com.explore.analyze.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.analyze.mappers.FlowHourMapper;
import com.explore.analyze.service.IFlowHourService;
import com.explore.common.database.FlowHour;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @ClassName FlowHourServiceImpl
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/2115:11
 * @Version 1.0
 **/
@Service
public class FlowHourServiceImpl extends ServiceImpl<FlowHourMapper, FlowHour> implements IFlowHourService {

    @Override
    public List<FlowHour> selectAllDayFlow(Long cameraId, LocalDate beginDate, LocalDate endDate) {
        return this.baseMapper.selectAllDayFlow(cameraId, beginDate, endDate);
    }
}
