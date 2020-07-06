package com.explore.client;

import com.explore.common.ServerResponse;
import com.explore.common.database.Camera;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author PinTeh
 * @date 2020/6/22
 */
@FeignClient(value = "detect-camera",fallback = CameraClientHystrix.class)
public interface CameraClient {

    /**
     * 获取摄像头参数信息
     * @param id cid
     * @return Camera
     */
    @GetMapping("/detect-camera/api/v1/camera")
    ServerResponse<Camera> getById(Long id);
}
