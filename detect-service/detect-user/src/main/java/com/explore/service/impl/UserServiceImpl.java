package com.explore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.common.Const;
import com.explore.authentication.JWTUtil;
import com.explore.common.ServerResponse;
import com.explore.entity.User;
import com.explore.form.ChangePassword;
import com.explore.form.UserQuery;
import com.explore.mappers.UserMapper;
import com.explore.service.IRoleService;
import com.explore.service.IUserService;
import com.explore.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author PinTeh
 * @date 2020/4/14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    IRoleService roleService;

    @Override
    public UserVo login(String username, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUsername, username)
                .eq(User::getPassword, password);
        User user = this.getOne(queryWrapper);
        if (user != null) {          // 登录成功
            // 使用用户id注册token, 使用用户密码加密
            String token = JWTUtil.encryptToken(JWTUtil.sign(user.getId().toString(), password));
            UserVo userVo = new UserVo();
            userVo.setToken(token);
            BeanUtils.copyProperties(user, userVo);
            userVo.setRoles(roleService.getRoles(user.getId()));
            return userVo;
        }
        // 登录失败
        return null;
    }

    @Override
    public ServerResponse register(User user) {
        if(user==null)
        {
            return ServerResponse.createByErrorMessage("注册失败!");
        }else if(StringUtils.isEmpty(user.getUsername())){
            return ServerResponse.createByErrorMessage("用户名不能为空!");
        }else if(StringUtils.isEmpty(user.getPassword())){
            return ServerResponse.createByErrorMessage("密码不能为空!");
        }else {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(User::getUsername, user.getUsername());
            if(this.getOne(queryWrapper) != null){
                return ServerResponse.createByErrorMessage("该用户名已被使用!");
            }
        }
        save(user);
        user.setPassword("");
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        return ServerResponse.createBySuccessMessage("register success", userVo);
    }

    @Override
    public ServerResponse changePassword(ChangePassword changePassword) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUsername, changePassword.getUsername())
                .eq(User::getPassword, changePassword.getOldPassword());
        User user = getOne(queryWrapper);
        Boolean result = saveOrUpdate(user);
        if(result){
            return ServerResponse.createBySuccess("密码修改成功!");
        }else{
            return ServerResponse.createBySuccess("密码修改失败!");
        }
    }

    @Override
    public Page<User> pageByQuery(UserQuery query) {
        Page page = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isEmpty(query.getUsername())){
            queryWrapper.eq(User::getUsername, query.getUsername());
        }
        return page(page, queryWrapper);
    }

    @Override
    public String flushToken() {
        User user = getById(this.getUserIdByToken());
        // 用户信息获取失败
        if (user == null) {
            return null;
        }
        // 用户信息获取成功
        // 使用用户id注册token, 使用用户密码加密
        String token = JWTUtil.encryptToken(JWTUtil.sign(user.getId().toString(), user.getPassword()));
        return token;
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
