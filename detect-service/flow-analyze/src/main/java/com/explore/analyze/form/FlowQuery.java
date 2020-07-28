package com.explore.analyze.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

    //    @NotNull(message = "摄像头id不能为空")
    private Long cameraId;

    /**
     * type值含义
     * WEEK：num天内人流量(默认)
     * WEEK：num周内人流量
     * MONTH：num月内人流量
     **/
    private Integer type = DAY;

    private Integer num = 1;        // 默认为1

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime = LocalDateTime.now();      // 默认当前时间
}
