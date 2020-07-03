package com.explore.client;

import com.explore.common.database.Camera;
import org.springframework.stereotype.Component;

/**
 * Hystrix
 * @author PinTeh
 * @date 2020/6/22
 */
@Component
public class UserClientHystrix implements UserClient {
    //TODO 获取用户
    @Override
    public Camera getById(Long id) {
        return Camera.invalid();
    }
}
