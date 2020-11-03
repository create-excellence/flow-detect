package com.explore.client;

import com.explore.common.ServerResponse;
import com.explore.common.database.Flow;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Primary
@FeignClient(value = "detect-flow")
public interface FlowClient {

    /**
     * 获取最新流
     * @return Camera
     */
    @GetMapping("/api/v1/flow")
    ServerResponse<Flow> getById(@RequestParam("cid") Integer cid);
}
