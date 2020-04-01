package com.yang.demo.model;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/2/27 23:13
 * Description: 表users的实体类
 */
@Data
public class User {
    private Integer id;
    private String nickname;
    private String avatar;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String loginName;
}