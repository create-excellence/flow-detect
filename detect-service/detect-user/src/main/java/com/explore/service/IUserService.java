package com.explore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vo.UserVo;
import com.explore.entity.User;

/**
 * @author PinTeh
 * @date 2020/4/14
 */
public interface IUserService extends IService<User> {
    UserVo login(String username, String password);

    Long getUserIdByToken();
}
