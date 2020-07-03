package com.explore.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.explore.common.ServerResponse;
import com.explore.common.database.Camera;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2020-06-28
 */
public interface ICameraService extends IService<Camera> {

    ServerResponse addCamera(Camera camera);

}
