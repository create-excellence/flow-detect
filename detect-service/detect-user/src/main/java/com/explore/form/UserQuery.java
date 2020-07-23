package com.explore.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @ClassName UserQuery
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1510:23
 * @Version 1.0
 **/
@Data
public class UserQuery {

    private Integer page = 1;

    private Integer size = 10;

    @Size(max = 25, message = "用户名的字符长度不能超过 {max}")
    private String username;
}
