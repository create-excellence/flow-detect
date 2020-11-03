package com.explore.analyze.client;

import com.explore.common.ServerResponse;
import com.explore.common.database.Camera;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author PinTeh
 * @date 2020/6/22
 */
@Primary
//@FeignClient(value = "detect-camera",fallback = CameraClientHystrix.class)
@FeignClient("detect-camera")
public interface CameraClient {

    /**
     * 获取摄像头参数信息
     * @param id cid
     * @return Camera
     */
    @GetMapping("/camera")
    ServerResponse<Camera> getById(@RequestParam(value = "id") Integer id);
}
