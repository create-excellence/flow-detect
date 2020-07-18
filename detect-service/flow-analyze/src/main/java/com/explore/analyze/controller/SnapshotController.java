package com.explore.analyze.controller;

import com.explore.analyze.form.SnapshotQuery;
import com.explore.analyze.service.ISnapshotService;
import com.explore.common.ServerResponse;
import com.explore.common.database.Snapshot;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @ClassName SnapshotController
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1518:06
 * @Version 1.0
 **/
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
        Boolean result = snapshotService.saveOrUpdate(snapshot);
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

}
