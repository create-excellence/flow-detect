package com.explore.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.explore.common.ServerResponse;
import com.explore.common.database.Warning;
import com.explore.servcie.IWarningService;
import org.springframework.web.bind.annotation.*;

/**
 * @author PinTeh
 * @date 2020/7/23
 */
@RestController
@RequestMapping("/api/v1/warning")
public class WarningController {



    private final IWarningService warningService;

    public WarningController(IWarningService warningService) {
        this.warningService = warningService;
    }

    @GetMapping
    public ServerResponse list(Integer cid, @RequestParam(required = false,defaultValue = "1")Integer page, @RequestParam(required = false,defaultValue = "10")Integer limit){
        Page<Warning> warningPage = warningService.page(new Page<>(page,limit),new QueryWrapper<Warning>().eq("camera_id",cid));
        return ServerResponse.createBySuccess(warningPage);
    }
}
