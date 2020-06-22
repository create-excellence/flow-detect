package com.explore.servcie.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.common.database.Flow;
import com.explore.mappers.FlowMapper;
import com.explore.servcie.IFlowService;
import org.springframework.stereotype.Service;

/**
 * @author PinTeh
 * @date 2020/6/22
 */
@Service
public class FlowServiceImpl extends ServiceImpl<FlowMapper, Flow> implements IFlowService {
}
