package com.yang.demo.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author Yiang37
 * @Date 2020/3/24 18:06
 * Description:
 */

@Data
public class MyQuestionAndPageDTO {
    List<MyQuestionDTO> myQuestionDTOList;
    private PageDTO pageDTO;
}