package com.explore.common.database;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName Snapshot
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1518:02
 * @Version 1.0
 **/
@Data
public class Snapshot {

    private Long id;

    private Long flowId;

    private String info;

    private Integer status;

    private String path;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
