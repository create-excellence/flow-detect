package com.explore.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @ClassName ChangePassword
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1510:15
 * @Version 1.0
 **/
@Data
public class ChangePassword {

    @NotBlank(message = "用户名不能为空")
    @Size(max = 25, message = "用户名的字符长度不能超过 {max}")
    private String username;

    @NotBlank(message = "旧密码不能为空")
    @Size(max = 25, message = "旧密码的字符长度不能超过 {max}")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(max = 25, message = "新密码的字符长度不能超过 {max}")
    private String newPassword;
}
