package com.explore.analyze.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.explore.analyze.form.SnapshotQuery;
import com.explore.analyze.vo.SnapshotVo;
import com.explore.common.ServerResponse;
import com.explore.common.database.Snapshot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @InterfaceName ISnapshotService
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1518:07
 * @Version 1.0
 **/
public interface ISnapshotService extends IService<Snapshot> {
    Page<SnapshotVo> pageByQuery(SnapshotQuery query);

    ServerResponse upload(MultipartFile file);

    ResponseEntity show(String fileName);

    /**
     * 保存快照记录v2
     * @param file 快照
     * @param cameraId 摄像头id
     * @param flowCount 人数
     */
    void saveV2(MultipartFile file, Long cameraId, Long flowCount);

    Page<Snapshot> pageByUser(Integer page, Integer limit);

}
