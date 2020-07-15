package com.explore.analyze.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.analyze.mappers.SnapshotMapper;
import com.explore.analyze.service.ISnapshotService;
import com.explore.common.database.Snapshot;
import org.springframework.stereotype.Service;

/**
 * @ClassName SnapshotServiceImpl
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1518:07
 * @Version 1.0
 **/
@Service
public class SnapshotServiceImpl extends ServiceImpl<SnapshotMapper, Snapshot> implements ISnapshotService {
}
