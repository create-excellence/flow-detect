package com.explore.controller;

import com.explore.common.ServerResponse;
import com.explore.entity.User;
import com.explore.form.ChangePassword;
import com.explore.form.UserQuery;
import com.explore.service.IUserService;
import com.explore.vo.UserVo;
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
    public ServerResponse login(@NotNull @RequestBody User user) {
        return this.userService.login(user.getUsername(), user.getPassword());
    }

    @PostMapping("/register")
    public ServerResponse register(@RequestBody User user) {
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
    public ServerResponse changePassword(@NotNull @RequestBody ChangePassword changePassword) {
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
    public ServerResponse add(@RequestBody User user) {
        return this.userService.register(user);
    }

    /**
     * @Author 安羽兮
     * @Description 通过id查找
     * @Date 13:42 2020/7/16
     * @Param [id]
     * @Return com.explore.common.ServerResponse
     **/
    @GetMapping("/{id:\\d+}")
    public ServerResponse getById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        if (null != user) {
            user.setPassword("");
            return ServerResponse.createBySuccess(user);
        } else {
            return ServerResponse.createByError();
        }
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
    public ServerResponse update(@NotNull @RequestBody UserVo userVo) {
        return userService.updateByUserVo(userVo);
//        User u = userService.getById(user.getId());
//        if (null != u) {
//            user.setPassword(u.getPassword());
//            Boolean result = userService.saveOrUpdate(user);
//            if (result) {
//                return ServerResponse.createBySuccess("用户信息更新成功");
//            }
//        }
//        return ServerResponse.createByErrorMessage("用户信息更新失败");
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
    public ServerResponse getUserId(@RequestParam("token") String token) {
        HashMap<String, Long> data = new HashMap();
        data.put("user_id", userService.getUserIdByToken(token));
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
    public ServerResponse flushToken(@RequestParam("token") String token) {
        String newToken = userService.flushToken(token);
        if (null != newToken) {
            HashMap<String, String> data = new HashMap();
            data.put("token", newToken);
            return ServerResponse.createBySuccessMessage("token刷新成功", data);
        } else {
            return ServerResponse.createByErrorMessage("token刷新失败");
        }
    }

}
