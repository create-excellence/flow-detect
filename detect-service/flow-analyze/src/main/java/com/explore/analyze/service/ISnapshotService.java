package com.explore.analyze.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.explore.analyze.form.SnapshotQuery;
import com.explore.analyze.vo.SnapshotVo;
import com.explore.common.database.Snapshot;

/**
 * @InterfaceName ISnapshotService
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1518:07
 * @Version 1.0
 **/
public interface ISnapshotService extends IService<Snapshot> {
    Page<SnapshotVo> pageByQuery(SnapshotQuery query);
}
