package com.yang.demo.dto;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/15 17:52
 * Description:
 * 编辑问题时的DTO
 *  //重新编辑时 需要问题标题 问题内容 问题标签
 */
@Data
public class QuestionEditDTO {
    private Integer id;
    private String title;
    private String description;
    private String tag;
}