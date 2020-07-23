package com.explore.analyze.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.explore.common.database.FlowHour;

import java.time.LocalDate;
import java.util.List;

public interface IFlowHourService extends IService<FlowHour> {
    List<FlowHour> selectAllDayFlow(Long cameraId, LocalDate beginDate, LocalDate endDate);
}
