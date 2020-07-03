package com.explore.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.explore.common.ServerResponse;
import com.explore.common.database.Camera;
import com.explore.service.ICameraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-06-28
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/camera")
public class CameraController {

    ICameraService cameraService;


    private  CameraController(ICameraService cameraService){
        this.cameraService=cameraService;
    }

    /**
     * 添加摄像头
     * @param camera obj
     */
    @PostMapping
    public ServerResponse addCamera(Camera camera){
        return cameraService.addCamera(camera);
    }

    /**
     * 修改摄像头
     * @param camera obj
     */
    @PutMapping
    public ServerResponse reviseCamera(Camera camera){
        camera.setToken(null);
        camera.setUserId(null);
        camera.setStatus(null);
        camera.setUpdateTime(LocalDateTime.now());

        return ServerResponse.createBySuccess(cameraService.saveOrUpdate(camera));
    }

    /**
     * 获取摄像头
     * @param id
     * @return
     */
    @GetMapping
    public ServerResponse get(Integer id){
        return ServerResponse.createBySuccess(
               cameraService.getById(id));
    }


    /**
     * 获得摄像头集合
     */
    @GetMapping("/list")
    public ServerResponse getList(@RequestParam(required = false,defaultValue = "1")Integer page,
                                      @RequestParam(required = false,defaultValue = "10")Integer limit){
        //TODO 获取UserId
        Integer userId = 1;

        return ServerResponse.createBySuccess(
                cameraService.page(
                        new Page<>(page,limit),
                        new QueryWrapper<Camera>().eq("user_id",userId).orderByDesc("id")));
    }

    @DeleteMapping("/{id}")
    public ServerResponse delete(@PathVariable("id")Integer id){
        return ServerResponse.createBySuccess(
             cameraService.removeById(id));
    }

}
