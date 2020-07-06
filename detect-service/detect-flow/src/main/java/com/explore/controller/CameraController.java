package com.explore.controller;

import com.explore.common.ServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PinTeh
 * @date 2020/7/4
 */
@RestController
@RequestMapping("/api/vi/camera")
public class CameraController {


    /**
     * 推流摄像头
     */
    @GetMapping("/open")
    public ServerResponse open(){

        return ServerResponse.createBySuccess();
    }

    /**
     * 断流摄像头
     */
    @GetMapping("/close")
    public ServerResponse close(){

        return ServerResponse.createBySuccess();
    }
}
