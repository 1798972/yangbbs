package com.yang.demo.dto;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/17 14:18
 * Description:
 * 评论的DTO
 */
@Data
public class CommentDTO {
    private Integer type;
    private Integer parentId;
    private Integer userId;
    private String content;
}