package com.explore.controller;

import com.explore.common.ServerResponse;
import com.explore.entity.User;
import com.explore.form.ChangePassword;
import com.explore.form.UserQuery;
import com.explore.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

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
    public ServerResponse login(@NotNull User user) {
        return this.userService.login(user.getUsername(), user.getPassword());
    }

    @PostMapping("/register")
    public ServerResponse register(User user) {
        return this.userService.register(user);
    }

    /**
     * @Author 安羽兮
     * @Description 修改密码
     * @Date 10:33 2020/7/15
     * @Param [changePassword]
     * @Return com.explore.common.ServerResponse
     **/
    @PostMapping("/change-password")
    public ServerResponse changePassword(@NotNull ChangePassword changePassword) {
        return this.userService.changePassword(changePassword);
    }

    /**
     * @Author 安羽兮
     * @Description 返回用户列表
     * @Date 9:58 2020/7/15
     * @Param [page, limit]
     * @Return com.explore.common.ServerResponse
     **/
    @GetMapping("/list")
    public ServerResponse list(UserQuery query) {
        return ServerResponse.createBySuccess(userService.pageByQuery(query));
    }

    /**
     * @Author 安羽兮
     * @Description 添加用户
     * @Date 10:04 2020/7/15
     * @Param [userId]
     * @Return com.explore.common.ServerResponse
     **/
    @PostMapping("/add")
    public ServerResponse add(User user) {
        return this.userService.register(user);
    }


    @DeleteMapping("/delete/{user_id:\\d+}")
    public ServerResponse delete(@PathVariable("user_id") Long userId) {
        Boolean result = userService.removeById(userId);
        if (result) {
            return ServerResponse.createBySuccess("删除成功");
        } else {
            return ServerResponse.createByErrorMessage("删除失败");
        }
    }

    /**
     * @Author 安羽兮
     * @Description //TODO
     * @Date 10:05 2020/7/15
     * @Param [user]
     * @Return com.explore.common.ServerResponse
     **/
    @PutMapping("/update")
    public ServerResponse update(@NotNull User user) {
        Boolean result = userService.saveOrUpdate(user);
        if (result) {
            return ServerResponse.createBySuccess("用户信息更新成功");
        } else {
            return ServerResponse.createByErrorMessage("用户信息更新失败");
        }
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
    public ServerResponse getUserId() {
        HashMap<String, Long> data = new HashMap();
        data.put("user_id", userService.getUserIdByToken());
        return ServerResponse.createBySuccess(data);
    }

    /**
     * @Author 安羽兮
     * @Description 通过请求头部携带的token, 刷新token有效时间, 返回token字符串
     * @Date 10:36 2020/7/15
     * @Param []
     * @Return com.explore.common.ServerResponse
     **/
    @GetMapping("flush-token")
    public ServerResponse flushToken() {
        String token = userService.flushToken();
        if (null != token) {
            return ServerResponse.createBySuccessMessage("token刷新成功", token);
        } else {
            return ServerResponse.createByErrorMessage("token刷新失败");
        }
    }

}
