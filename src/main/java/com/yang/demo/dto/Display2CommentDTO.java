package com.yang.demo.dto;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/20 0:22
 * Description:
 * 二级评论
 */
@Data
public class Display2CommentDTO {
    private String pid;
    private String avatar;
    private String nickname;
    private Long gmtCreate;
    private String content;
}