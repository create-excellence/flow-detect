package com.explore.controller;

import com.explore.client.CameraClient;
import com.explore.common.ServerResponse;
import com.explore.common.database.Camera;
import com.explore.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PinTeh
 * @date 2020/7/4
 */
@RestController
@RequestMapping("/api/v1/camera")
public class CameraController {

    private final String rtspPath = "rtsp://%s:%s@%s:%s/h264/ch1/main/av_stream";
    private final String pushUrl = "rtsp://%s:%s@%s:%s/h264/ch1/main/av_stream";

    private final String path = "http://localhost:9090";

    private final CameraClient cameraClient;

    public CameraController(CameraClient cameraClient) {
        this.cameraClient = cameraClient;
    }

    /**
     * 推流摄像头
     */
    @GetMapping("/start")
    public ServerResponse start(Integer cid){
        ServerResponse<Camera> serverResponse = cameraClient.getById(cid);
        Camera camera = serverResponse.getData();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", String.valueOf(cid));
        params.add("source", camera.getSource());
        params.add("pushUrl", camera.getPushUrl());
        String startUrl = path + "/camera/start";
        return HttpUtils.post(startUrl, params);
    }

    @GetMapping("/test")
    public ServerResponse test(Integer cid){
        ServerResponse<Camera> serverResponse = cameraClient.getById(cid);
        Camera camera = serverResponse.getData();
        System.out.println(camera.toString());
        return ServerResponse.createBySuccess();
    }

    /**
     * 断流摄像头
     */
    @GetMapping("/close")
    public ServerResponse close(Integer cid){
        // TODO : check
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", String.valueOf(cid));
        String stopUrl = path + "/camera/stop";
        return HttpUtils.post(stopUrl,params);
    }
}
