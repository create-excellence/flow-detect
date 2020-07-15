package com.explore.analyze.controller;

import com.explore.common.ServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName SnapshotContraller
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1518:06
 * @Version 1.0
 **/
@Controller
@RequestMapping("/snapshot")
public class SnapshotContraller {

    /**
     * @Author 安羽兮
     * @Description 异常人流快照
     * @Date 17:53 2020/7/15
     * @Param []
     * @Return com.explore.common.ServerResponse
     **/
    @GetMapping("/snapshot")
    public ServerResponse snapshot() {
        return ServerResponse.createBySuccessMessage("success");
    }
}
