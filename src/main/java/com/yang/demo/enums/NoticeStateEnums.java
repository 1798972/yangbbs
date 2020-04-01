package com.yang.demo.enums;

/**
 * @Author Yiang37
 * @Date 2020/3/25 13:56
 * Description:
 * 已读0 未读1
 */
public enum NoticeStateEnums {
    READ(0),
    UN_READ(1);
    private int state;

    NoticeStateEnums(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}