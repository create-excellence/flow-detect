package com.explore.client;

import com.explore.common.ServerResponse;
import com.explore.common.database.Camera;
import org.springframework.stereotype.Component;

/**
 * Hystrix
 * @author PinTeh
 * @date 2020/6/22
 */
@Component
public class CameraClientHystrix implements CameraClient {

    @Override
    public ServerResponse<Camera> getById(Long id) {
        return ServerResponse.createBySuccess(Camera.invalid());
    }
}
