package com.explore.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.explore.common.Const;
import com.explore.common.annotation.Permission;
import com.explore.vo.UserVo;
import com.explore.common.ServerResponse;
import com.explore.entity.User;
import com.explore.service.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
        return ServerResponse.createBySuccessMessage("login success", userVo);
    }

    /**
     * @Author 安羽兮
     * @Description 发送请求时请求头附带token, 进而获取用户id
     * @Date 18:21 2020/6/30
     * @Param []
     * @Return com.explore.common.ServerResponse
     **/
//    @Permission(roles = {Const.ADMIN})
    @GetMapping("/getUserId")
    public ServerResponse getUserId(){
        HashMap<String, Long> data = new HashMap();
        data.put("user_id", userService.getUserIdByToken());
        return ServerResponse.createBySuccess(data);
    }

    @GetMapping()
    public ServerResponse foo(){
        Page<User> page = userService.page(new Page<>(1, 10));
        return ServerResponse.createBySuccess(page);
    }
}
