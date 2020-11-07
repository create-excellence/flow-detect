package com.explore.servcie;

import com.baomidou.mybatisplus.extension.service.IService;
import com.explore.common.ServerResponse;
import com.explore.common.database.Flow;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author PinTeh
 * @date 2020/6/22
 */
public interface IFlowService extends IService<Flow> {
    void saveData(Flow flow , MultipartFile file);

    ServerResponse getUserCameraAndFlow();
}
