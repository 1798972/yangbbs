package com.yang.demo.dto;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/4 17:06
 * Description:
 * 注册成功/失败的状态
 */

@Data
public class RegisterStateDTO {
    private String state;
    private int code;
    private String des;
}