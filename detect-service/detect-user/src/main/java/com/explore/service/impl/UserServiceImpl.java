package com.explore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.entity.User;
import com.explore.mappers.UserMapper;
import com.explore.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * @author PinTeh
 * @date 2020/4/14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
}
