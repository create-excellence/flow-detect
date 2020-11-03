package com.explore.analyze.client;

import com.explore.common.ServerResponse;
import com.explore.common.database.Camera;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Hystrix
 * @author PinTeh
 * @date 2020/6/22
 */
@Slf4j
@Component
public class CameraClientHystrix implements CameraClient {

    @Override
    public ServerResponse<Camera> getById(Integer id) {
        log.warn("获取Camera失败,id = {}",id);
        return ServerResponse.createBySuccess(Camera.invalid());
    }
}
