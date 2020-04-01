package com.yang.demo.enums;

/**
 * @Author Yiang37
 * @Date 2020/3/1 0:17
 * Description:
 * 用户类型的枚举
 * 0网站用户 1QQ 2GitHub 3手机
 */
public enum UserTypeEnums {
    WebUser(0),
    QQUser(1),
    GitHubUser(2),
    TelUser(3);

    private int userType;

    UserTypeEnums(int userType) {
        this.userType = userType;
    }

    public int getUserType() {
        return userType;
    }
}