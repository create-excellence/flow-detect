package com.explore.common.database;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2020-06-28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Camera implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String source;

    private String pushUrl;

    private String cover;

    private String ip;


    /**
     * 摄像头编号
     */
    private String code;

    /**
     * 摄像头具体放置地点
     */
    private String position;

    private String token;

    private Integer status;

    private Integer userId;

    private Integer warning;

    /**
     * 所属组织
     */
    private Integer organizationId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public static Camera invalid(){
        Camera camera = new Camera();
        camera.setId(-1);
        camera.setCode("");
        return camera;
    }

}
