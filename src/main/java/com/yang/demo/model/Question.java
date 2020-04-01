package com.yang.demo.model;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/7 21:07
 * Description:
 * 问题 实体类
 */
@Data
public class Question {
    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
}