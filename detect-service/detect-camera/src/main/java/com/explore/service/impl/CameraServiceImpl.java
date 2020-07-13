package com.explore.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.common.ServerResponse;
import com.explore.common.database.Camera;
import com.explore.mapper.CameraMapper;
import com.explore.service.ICameraService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-06-28
 */
@Service
public class CameraServiceImpl extends ServiceImpl<CameraMapper, Camera> implements ICameraService {

    public final static int OPEN = 0 ;
    public final static int CLOSE = 1 ;

    @Override
    public ServerResponse addCamera(Camera camera) {
        camera.setCreateTime(LocalDateTime.now());
        camera.setUpdateTime(LocalDateTime.now());
//        camera.setStatus(OPEN);
        camera.setToken(getToken());
        if(this.save(camera)){
            return ServerResponse.createBySuccessMessage("添加成功");
        }
        return ServerResponse.createByErrorMessage("添加失败");
    }

    private  String getToken(){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        while (true){
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<16;i++){
                int number=random.nextInt(62);
                sb.append(str.charAt(number));
            }
            int count = baseMapper.selectCount(new QueryWrapper<Camera>().eq("token",sb.toString()));
            if(count == 0 ) return sb.toString();
        }

    }
}
