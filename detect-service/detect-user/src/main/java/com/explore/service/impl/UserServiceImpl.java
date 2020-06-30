package com.explore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.common.Const;
import com.vo.UserVo;
import com.explore.authentication.JWTUtil;
import com.explore.entity.User;
import com.explore.mappers.UserMapper;
import com.explore.service.IRoleService;
import com.explore.service.IUserService;
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
