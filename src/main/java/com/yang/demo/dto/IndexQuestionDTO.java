package com.yang.demo.dto;

import com.yang.demo.model.Question;
import com.yang.demo.model.User;
import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/13 16:29
 * Description:
 * 首页展示问题DTO
 */
@Data
public class IndexQuestionDTO {
    private Integer id;
    private String userIcon;
    private String title;
    private String description;
    private Long gmtCreate;
}
