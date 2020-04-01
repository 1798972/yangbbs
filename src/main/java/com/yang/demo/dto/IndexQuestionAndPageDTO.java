package com.yang.demo.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author Yiang37
 * @Date 2020/3/13 22:50
 * Description:
 * 首页展示问题时包含了页码
 */
@Data
public class IndexQuestionAndPageDTO {
    List<IndexQuestionDTO> indexQuestionDTOList;
    private PageDTO pageDTO;
}