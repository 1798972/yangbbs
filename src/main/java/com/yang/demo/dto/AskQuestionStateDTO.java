package com.yang.demo.dto;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/7 21:27
 * Description:
 * 在发布问题时 返回状态DTO
 */

@Data
public class AskQuestionStateDTO {
    private String state;
    private Integer errorCode;
    private String des;
}