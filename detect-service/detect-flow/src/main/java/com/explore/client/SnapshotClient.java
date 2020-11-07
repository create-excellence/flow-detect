package com.explore.client;

import com.explore.common.ServerResponse;
import com.explore.common.database.Camera;
import com.explore.common.database.Snapshot;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author PinTeh
 * @date 2020/6/22
 */
@Primary
@FeignClient("flow-analyze")
public interface SnapshotClient {

    /**
     * 添加快照
     *
     * @return Camera
     */
    @GetMapping("/snapshot/add")
    ServerResponse add(@RequestBody Snapshot snapshot);

}
