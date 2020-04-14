package com.explore.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author PinTeh
 * @date 2020/4/14
 */
@Data
@TableName("user")
public class User {

    private Long id;

    private String username;

    private String password;
}
