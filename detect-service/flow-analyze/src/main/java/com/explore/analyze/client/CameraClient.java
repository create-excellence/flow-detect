package com.explore.analyze.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@FeignClient("detect-camera")
public interface CameraClient {

    /**
     * 获取摄像头参数信息
     * @param id cid
     * @return Camera
     */
    @GetMapping("/camera")
    ServerResponse<Camera> getById(@RequestParam(value = "id") Integer id);

    /**
     * 根据用户ID获取摄像头列表
     * @param page 页码
     * @param limit 页面大小
     * @return 分页数据
     */
    @GetMapping("/camera/list/all")
    ServerResponse<Page<Camera>> getPageByUserId(@RequestParam(required = false,defaultValue = "1")Integer page,
                                               @RequestParam(required = false,defaultValue = "10")Integer limit,
                                                 @RequestParam Integer userId);

}
