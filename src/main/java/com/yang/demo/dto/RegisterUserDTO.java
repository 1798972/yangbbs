package com.yang.demo.dto;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/4 0:43
 * Description:
 * 注册时收到的用户 要与前端请求中的键名对应 是严格一样
 * 大小写/加数字 都不行
 */

@Data
public class RegisterUserDTO {
    private String username;
    private String password;
    private String tel;
}