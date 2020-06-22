package com.explore.common.database;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author PinTeh
 * @date 2020/6/22
 */
@Data
public class Camera {

    private Long id;

    private String code;

    private String position;

    private Long organizationId;

    private Integer warning;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public static Camera invalid(){
        Camera camera = new Camera();
        camera.setId(-1L);
        camera.setCode("");
        return camera;
    }
}
