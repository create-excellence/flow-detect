package com.explore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.explore.entity.Role;

import java.util.List;

/**
 * @InterfaceName IRoleService
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/6/3010:19
 * @Version 1.0
 **/
public interface IRoleService extends IService<Role> {
    List<String> getRoles(Long userId);
}
