package com.explore.analyze.form;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName FlowQuery
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1518:11
 * @Version 1.0
 **/
@Data
public class FlowQuery {

    // 一天内
    public static final Integer DAY = 1;

    // 一周内
    public static final Integer WEEK = 2;

    // 一月内
    public static final Integer MONTH = 3;

    private Long cameraId;

    /**
     * type值含义
     * WEEK：一天内人流量(默认)
     * WEEK：一周内人流量
     * MONTH：一月内人流量
     **/
    private Integer type = DAY;

    private LocalDateTime beginTime;

    private LocalDateTime endTime = LocalDateTime.now();      // 默认当前时间
}
