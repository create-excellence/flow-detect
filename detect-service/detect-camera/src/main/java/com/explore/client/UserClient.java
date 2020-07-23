package com.explore.client;

import com.explore.common.database.Camera;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author PinTeh
 * @date 2020/6/22
 */
@Primary
@FeignClient(value = "detect-user")
public interface UserClient {

    /**
     * 获取用户id
     * @return Camera
     */
    @GetMapping("/user/getUserId")
    Map getById(@RequestParam("token") String token);
}
