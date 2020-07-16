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

    // 天
    public static final Integer DAY = 1;

    // 周
    public static final Integer WEEK = 2;

    // 月
    public static final Integer MONTH = 3;

    private Long cameraId;

    /**
     * type值含义
     * WEEK：num天内人流量(默认)
     * WEEK：num周内人流量
     * MONTH：num月内人流量
     **/
    private Integer type = DAY;

    private Integer num = 1;        // 默认为1

    private LocalDateTime beginTime;

    private LocalDateTime endTime = LocalDateTime.now();      // 默认当前时间
}
