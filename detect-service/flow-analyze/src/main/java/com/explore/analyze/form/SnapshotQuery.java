package com.explore.analyze.form;

import lombok.Data;

/**
 * @ClassName SnapshotQuery
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1610:58
 * @Version 1.0
 **/
@Data
public class SnapshotQuery {

    private Integer page = 1;

    private Integer size = 10;

    private Integer flow;   // 根据人流量筛选

    private Long organizationId; // 根据所属组织筛选
}
