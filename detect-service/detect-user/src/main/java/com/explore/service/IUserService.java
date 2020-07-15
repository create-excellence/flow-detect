package com.explore.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.explore.common.ServerResponse;
import com.explore.form.ChangePassword;
import com.explore.form.UserQuery;
import com.explore.vo.UserVo;
import com.explore.entity.User;

/**
 * @author PinTeh
 * @date 2020/4/14
 */
public interface IUserService extends IService<User> {
    UserVo login(String username, String password);

    Long getUserIdByToken();

    ServerResponse register(User user);

    ServerResponse changePassword(ChangePassword changePassword);

    Page<User> pageByQuery(UserQuery query);

    String flushToken();
}
