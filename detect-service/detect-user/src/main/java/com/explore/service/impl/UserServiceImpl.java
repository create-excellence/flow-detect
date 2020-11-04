package com.explore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.authentication.JWTUtil;
import com.explore.common.Const;
import com.explore.common.ServerResponse;
import com.explore.entity.Role;
import com.explore.entity.User;
import com.explore.form.ChangePassword;
import com.explore.form.UserQuery;
import com.explore.mappers.UserMapper;
import com.explore.service.IRoleService;
import com.explore.service.IUserService;
import com.explore.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PinTeh
 * @date 2020/4/14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final IRoleService roleService;

    public UserServiceImpl(IRoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public ServerResponse login(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            return ServerResponse.createByErrorMessage("用户名不能为空!");
        } else if (StringUtils.isEmpty(password)) {
            return ServerResponse.createByErrorMessage("密码不能为空!");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUsername, username)
                .eq(User::getPassword, password);
        User user = this.getOne(queryWrapper);
        if (null != user) {          // 登录成功
            // 使用用户id注册token, 使用用户密码加密
            String token = JWTUtil.encryptToken(JWTUtil.sign(user.getId().toString(), password));
            UserVo userVo = new UserVo();
            userVo.setToken(token);
            BeanUtils.copyProperties(user, userVo);
            userVo.setPassword("");
            userVo.setRoles(roleService.getRoles(user.getId()));
            return ServerResponse.createBySuccessMessage("login success", userVo);
        }
        // 登录失败
        return ServerResponse.createByErrorMessage("用户名或密码错误");
    }

    @Override
    public ServerResponse register(User user) {
        if (null == user) {
            return ServerResponse.createByErrorMessage("注册失败!");
        } else if (StringUtils.isEmpty(user.getUsername())) {
            return ServerResponse.createByErrorMessage("用户名不能为空!");
        } else if (StringUtils.isEmpty(user.getPassword())) {
            return ServerResponse.createByErrorMessage("密码不能为空!");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUsername, user.getUsername());
        if (this.getOne(queryWrapper) != null) {
            return ServerResponse.createByErrorMessage("该用户名已被使用!");
        }
        save(user);
        user.setPassword("");
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return ServerResponse.createBySuccessMessage("register success", userVo);
    }

    @Override
    public ServerResponse changePassword(ChangePassword changePassword) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUsername, changePassword.getUsername())
                .eq(User::getPassword, changePassword.getOldPassword());
        User user = getOne(queryWrapper);
        if(user == null)  return ServerResponse.createByErrorMessage("原密码不正确");
        user.setPassword(changePassword.getNewPassword());
        Boolean result = saveOrUpdate(user);
        if (result) {
            return ServerResponse.createBySuccess("密码修改成功!");
        } else {
            return ServerResponse.createByErrorMessage("密码修改失败!");
        }
    }

    @Override
    public Page<UserVo> pageByQuery(UserQuery query) {
        Page page = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        if (!StringUtils.isEmpty(query.getUsername())) {
            queryWrapper.eq(User::getUsername, query.getUsername());
        }
        Page data = page(page, queryWrapper);
        List<UserVo> userVos = new ArrayList<>();
        data.getRecords().stream().forEach(user -> {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            List roles = roleService.getRoles(((User) user).getId());
            userVo.setRoles(roles);
            userVo.setPassword("");
            userVos.add(userVo);
        });
        data.setRecords(userVos);
        return data;
    }

    @Override
    public String flushToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(Const.TOKEN);
        return this.flushToken(token);
    }

    @Override
    public String flushToken(@NotNull String token) {
        User user = getById(this.getUserIdByToken(token));
        // 用户信息获取失败
        if (user == null) {
            return null;
        }
        // 用户信息获取成功
        // 使用用户id重新注册token, 使用用户密码加密
        String newToken = JWTUtil.encryptToken(JWTUtil.sign(user.getId().toString(), user.getPassword()));
        return newToken;
    }

    @Override
    public ServerResponse updateByUserVo(UserVo userVo) {
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        User u = getById(user.getId());
        if (null != u) {
            user.setPassword(u.getPassword());
            Boolean result = saveOrUpdate(user);
            if (result) {
                // 更新用户角色信息
                if (userVo.getRoles() != null && userVo.getRoles().size() > 0)
                    this.updateUserRoles(userVo);
                return ServerResponse.createBySuccess("用户信息更新成功");
            }
        }
        return ServerResponse.createByErrorMessage("用户信息更新失败");
    }

    /**
     * @Author 安羽兮
     * @Description 通过传入的UserVo更新对应用户的角色
     * @Date 12:48 2020/7/18
     * @Param [userVo]
     * @Return void
     **/
    @Override
    public void updateUserRoles(@NotNull UserVo userVo) {
        List<String> roles = new ArrayList<>();
        List<String> oldRoles = roleService.getRoles(userVo.getId());
        List<String> newRoles = userVo.getRoles();

        roles.add(Const.USER);
        roles.add(Const.ADMIN);
        oldRoles.forEach(role -> {
            if (!newRoles.contains(role)) {       // 需要移除该角色
                LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(Role::getUserId, userVo.getId())
                        .eq(Role::getRole, role);
                roleService.remove(queryWrapper);
            }
        });
        newRoles.forEach(role -> {
            if (roles.contains(role) && !oldRoles.contains(role)) {       // 需要添加该角色
                Role r = new Role();
                r.setUserId(userVo.getId());
                r.setRole(role);
                roleService.save(r);
            }
        });
        return;
    }

    /**
     * @Author 安羽兮
     * @Description 通过token获取用户id
     * @Date 11:37 2020/6/30
     * @Param []
     * @Return java.lang.Long
     **/
    @Override
    public Long getUserIdByToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(Const.TOKEN);
        return getUserIdByToken(token);
    }

    /**
     * @Author 安羽兮
     * @Description 直接解析传入的token
     * @Date 14:30 2020/7/16
     * @Param [token]
     * @Return java.lang.Long
     **/
    @Override
    public Long getUserIdByToken(String token) {
        if (StringUtils.isEmpty(token))
//            throw new AuthenticationException("请登录后访问！");
            return null;
        token = JWTUtil.decryptToken(token);            // 解密token
        String id = JWTUtil.getName(token);
        User user = this.getById(id);
        // 校验token是否合法
        if (!JWTUtil.verify(token, id, user.getPassword()))
//            throw new AuthenticationException("token校验不通过");
            return null;
        return Long.parseLong(id);
    }
}
