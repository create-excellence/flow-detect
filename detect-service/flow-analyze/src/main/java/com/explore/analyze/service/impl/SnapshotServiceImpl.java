package com.explore.analyze.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.analyze.form.SnapshotQuery;
import com.explore.analyze.mappers.SnapshotMapper;
import com.explore.analyze.service.IFlowService;
import com.explore.analyze.service.ISnapshotService;
import com.explore.analyze.vo.SnapshotVo;
import com.explore.common.database.Flow;
import com.explore.common.database.Snapshot;
import com.explore.service.ICameraService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SnapshotServiceImpl
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1518:07
 * @Version 1.0
 **/
@Service
public class SnapshotServiceImpl extends ServiceImpl<SnapshotMapper, Snapshot> implements ISnapshotService {

    private final IFlowService flowService;

    private final ICameraService cameraService;

    public SnapshotServiceImpl(IFlowService flowService, ICameraService cameraService) {
        this.flowService = flowService;
        this.cameraService = cameraService;
    }


    @Override
    public Page<SnapshotVo> pageByQuery(SnapshotQuery query) {
        Page page = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<Snapshot> queryWrapper = new LambdaQueryWrapper();
        Page<SnapshotVo> data = page(page, queryWrapper);
        List<SnapshotVo> snapshotVos = new ArrayList<>();
        data.getRecords().stream().forEach(snapshotVo -> {
            Flow flow = flowService.getById(snapshotVo.getFlowId());
            snapshotVo.setFlow(flow);
            snapshotVo.setCamera(cameraService.getById(flow.getCameraId()));
            // 满足筛选条件才加入集合
            if (null == query.getFlow() || flow.getFlow().intValue() >= query.getFlow().intValue()) {
                if (null == query.getOrganizationId() || snapshotVo.getCamera().getOrganizationId().equals(query.getOrganizationId()))
                    snapshotVos.add(snapshotVo);
            }
        });
        data.setRecords(snapshotVos);
        return data;
    }
}
