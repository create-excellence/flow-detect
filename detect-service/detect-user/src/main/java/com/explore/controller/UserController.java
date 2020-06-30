package com.explore.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vo.UserVo;
import com.explore.common.ServerResponse;
import com.explore.entity.User;
import com.explore.service.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PinTeh
 * @date 2020/4/14
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ServerResponse login(User user){
        UserVo userVo = this.userService.login(user.getUsername(),user.getPassword());
        return ServerResponse.createBySuccessMessage("success", userVo);
    }

    @GetMapping()
    public ServerResponse foo(){
        Page<User> page = userService.page(new Page<>(1, 10));
        return ServerResponse.createBySuccess(page);
    }
}
