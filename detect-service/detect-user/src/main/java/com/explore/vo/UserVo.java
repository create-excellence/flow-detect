package com.explore.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName UserVo
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/6/3010:49
 * @Version 1.0
 **/
@Data
public class UserVo {

    String username;

    String token;

    List<String> roles;
}
