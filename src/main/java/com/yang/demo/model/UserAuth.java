package com.yang.demo.model;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/1 0:11
 * Description: 表user_auths的实体类
 */

@Data
public class UserAuth {
    private Integer id;
    private Integer userId;
    //用户类型: 0网站用户 1手机用户 2邮箱 3QQ 4GitHub
    private Integer uType;
    private String verification;
}