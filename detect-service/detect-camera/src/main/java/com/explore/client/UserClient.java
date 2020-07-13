package com.explore.client;

import com.explore.common.database.Camera;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * @author PinTeh
 * @date 2020/6/22
 */
@FeignClient(name = "detect-user",fallback = UserClientHystrix.class)
public interface UserClient {

    /**
     * 获取用户id
     * @return Camera
     */
    @GetMapping("/user/getUserId")
    Map<String,Integer> getById();
}
