package com.explore.analyze.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.explore.common.database.FlowHour;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

public interface FlowHourMapper extends BaseMapper<FlowHour> {

    // 这里统计的是一天的人流量，依旧保存在hour_flow
    @Select("SELECT fh.camera_id, fh.date, sum(fh.hour_flow) AS hour_flow FROM flow_hour fh WHERE fh.camera_id = #{cameraId} and fh.date = #{date}")
    FlowHour selectAllDayFlow(@Param("cameraId") Long cameraId, @Param("date") LocalDate date);

    // 这里统计的是一天的人流量，依旧保存在hour_flow
    @Select("SELECT fh.camera_id, fh.date, sum(fh.hour_flow) AS hour_flow FROM flow_hour fh WHERE fh.camera_id = #{cameraId} and fh.beginDate > #{beginDate} and fh.date < #{endDate} group by fh.date")
    List<FlowHour> selectAllDayFlow(@Param("cameraId") Long cameraId, @Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);
}
