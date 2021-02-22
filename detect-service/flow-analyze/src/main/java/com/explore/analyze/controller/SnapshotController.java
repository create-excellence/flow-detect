package com.explore.analyze.controller;

import com.explore.analyze.form.SnapshotQuery;
import com.explore.analyze.service.ISnapshotService;
import com.explore.common.ServerResponse;
import com.explore.common.database.Snapshot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName SnapshotController
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1518:06
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/snapshot")
public class SnapshotController {

    private final ISnapshotService snapshotService;

    public SnapshotController(ISnapshotService snapshotService) {
        this.snapshotService = snapshotService;
    }

    /**
     * @Author 安羽兮
     * @Description 添加快照
     * @Date 13:17 2020/7/16
     * @Param [snapshot]
     * @Return com.explore.common.ServerResponse
     **/
    @PostMapping("/add")
    public ServerResponse add(@RequestBody Snapshot snapshot) {
        // todo 保存上传的快照
        boolean result = snapshotService.saveOrUpdate(snapshot);
        if (result) {
            return ServerResponse.createBySuccessMessage("添加成功");
        }
        return ServerResponse.createByErrorMessage("添加失败");
    }


    @DeleteMapping("/delete/{id:\\d+}")
    public ServerResponse delete(@PathVariable("id") Long id) {
        Boolean result = snapshotService.removeById(id);
        if (result) {
            return ServerResponse.createBySuccess("删除成功");
        } else {
            return ServerResponse.createByErrorMessage("删除失败");
        }
    }

    @PutMapping("/update")
    public ServerResponse update(@NotNull @RequestBody Snapshot snapshot) {
        Boolean result = snapshotService.saveOrUpdate(snapshot);
        if (result) {
            return ServerResponse.createBySuccess("更新成功");
        } else {
            return ServerResponse.createByErrorMessage("更新失败");
        }
    }

    /**
     * @Author 安羽兮
     * @Description 通过id查找
     * @Date 13:42 2020/7/16
     * @Param [id]
     * @Return com.explore.common.ServerResponse
     **/
    @GetMapping("/{id:\\d+}")
    public ServerResponse getById(@PathVariable("id") Long id) {
        Snapshot snapshot = snapshotService.getById(id);
        if (null != snapshot) {
            return ServerResponse.createBySuccess(snapshot);
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * @Author 安羽兮
     * @Description 返回异常人流快照列表
     * @Date 17:53 2020/7/15
     * @Param [query]
     * @Return com.explore.common.ServerResponse
     **/
    @GetMapping("/list")
    public ServerResponse snapshot(SnapshotQuery query) {
        return ServerResponse.createBySuccess(snapshotService.pageByQuery(query));
    }

    /**
     * @Author 安羽兮
     * @Description 上传图片
     * @Date 15:26 2020/7/26
     * @Param [file]
     * @Return com.explore.common.ServerResponse
     **/
    @PostMapping("/upload")
    public ServerResponse editMovieInfo(@RequestParam("file") MultipartFile file) {
        return snapshotService.upload(file);
    }

    /**
     * 显示单张图片
     */
    @GetMapping("/show/{fileName}")
    public ResponseEntity showPhotos(@PathVariable("fileName") String fileName) {
        return snapshotService.show(fileName);
    }

    /**
     * 保存快照信息v2
     */
    @PostMapping("/v2/save")
    public void saveV2(@RequestParam MultipartFile file,@RequestParam Long cameraId,@RequestParam Long flowCount){
        snapshotService.saveV2(file,cameraId,flowCount);
    }
}
