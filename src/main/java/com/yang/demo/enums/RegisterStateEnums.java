package com.yang.demo.enums;

public enum RegisterStateEnums {
    REGISTER_SUCCESS(9000,"注册成功"),
    REGISTER_ERROR(9001,"注册成功"),
    USERNAMW_FORMAT_ERROR(9102,"用户名格式错误!"),
    PASSWOED_FORMAT_ERROR(9103,"密码格式错误!"),
    TEL_FORMAT_ERROR(9104,"手机格式错误!"),
    USERNAME_HAS_REGISTER(9105,"用户名已被注册!"),
    USERINFO_HAS_NULL(9106,"用户注册信息不完整!");

    private int code;
    private String des;

    RegisterStateEnums(int code, String des) {
        this.code = code;
        this.des = des;
    }

    public int getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
