package com.explore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.common.Const;
import com.explore.entity.Role;
import com.explore.mappers.RoleMapper;
import com.explore.service.IRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName RoleServiceImpl
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/6/3010:20
 * @Version 1.0
 **/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Override
    public List<String> getRoles(Long userId) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Role::getUserId, userId);
        Page<Role> page = new Page();
        page.setTotal(10);
        List<Role> roles = this.page(page, queryWrapper).getRecords();
        if (roles.size() == 0) {      // 用户身份默认为user
            Role r = new Role();
            r.setRole(Const.USER);
            roles.add(r);
        }
        return roles.stream().map(role -> role.getRole()).collect(Collectors.toList());
    }


}
