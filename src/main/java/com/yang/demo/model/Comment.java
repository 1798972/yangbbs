package com.yang.demo.model;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/17 14:22
 * Description:
 */
@Data
public class Comment {
    private Integer id;
    private Integer parentId;
    private Integer ctype;
    private Integer userId;
    private String content;
    private Integer likeCount;
    private Integer commentCount;
    private Long gmtCreate;
    private Long gmtModified;
}