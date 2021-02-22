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

    private Long flowCount;

    private Long flowId;

    private Long cameraId;

    private String info;

    private Integer status;

    private String path;

    private String fileName;

    private LocalDateTime recordTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Snapshot(){
        this.recordTime = LocalDateTime.now();
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
}
