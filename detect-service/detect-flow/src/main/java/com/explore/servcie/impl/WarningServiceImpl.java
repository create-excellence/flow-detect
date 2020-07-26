package com.explore.servcie.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.common.database.Warning;
import com.explore.mappers.WarningMapper;
import com.explore.servcie.IWarningService;
import org.springframework.stereotype.Service;

/**
 * @author PinTeh
 * @date 2020/7/23
 */
@Service
public class WarningServiceImpl extends ServiceImpl<WarningMapper, Warning> implements IWarningService {
}
