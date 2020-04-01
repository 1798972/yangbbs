package com.yang.demo.exception;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/31 22:04
 * Description:
 */
@Data
public class CustomizeException extends RuntimeException {

    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }
}