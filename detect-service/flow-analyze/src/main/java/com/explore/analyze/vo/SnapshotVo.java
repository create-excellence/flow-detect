package com.explore.analyze.vo;

import com.explore.common.database.Camera;
import com.explore.common.database.Flow;
import com.explore.common.database.Snapshot;
import lombok.Data;

/**
 * @ClassName SnapshotVo
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1610:40
 * @Version 1.0
 **/
@Data
public class SnapshotVo extends Snapshot {

    private Flow flow;

    private Camera camera;
}
