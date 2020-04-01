package com.yang.demo.enums;

public enum TelStateEnums {

    LENGTH_ERROR(9001,"手机号不是11位数吗？"),
    FORMAT_ERROR(9002,"请使用真实手机号!"),
    SEND_SMS_ERROR(9003,"可能是没钱发短信了..."),
    TEL_USED(9004,"该手机号已经被注册过!");

    private int errorCode;
    private String des;

    TelStateEnums(int errorCode, String des) {
        this.errorCode = errorCode;
        this.des = des;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getDes() {
        return des;
    }
}
