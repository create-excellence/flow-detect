package com.explore.common.database;

import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName FlowHour
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/2115:03
 * @Version 1.0
 **/
@Data
public class FlowHour {
    private Long cameraId;

    private LocalDate date;

    private Integer hour;

    private Integer hourFlow;
}
