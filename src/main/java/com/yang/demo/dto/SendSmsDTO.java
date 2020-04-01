package com.yang.demo.dto;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/2 22:47
 * Description:
 */
@Data
public class SendSmsDTO {

    // success /error
    private String status;
    //回传码 0成功 其他为错误码
    private int backCode;
    //详细信息
    private String des;
}