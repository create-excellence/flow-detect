package com.explore.common.database;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author PinTeh
 * @date 2020/6/22
 */
@Data
public class Flow {

    private Long id;

    private Integer flow;

    private Long cameraId;

    private String photoAddress;

    private Integer detectStatus;

    private LocalDateTime currentTime;

    private LocalDateTime createTime;
}
