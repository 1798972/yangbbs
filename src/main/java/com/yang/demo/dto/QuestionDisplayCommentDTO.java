package com.yang.demo.dto;

import lombok.Data;
import org.omg.PortableInterceptor.INACTIVE;

/**
 * @Author Yiang37
 * @Date 2020/3/17 21:24
 * Description:
 *
 * 问题展示界面的DTO
 */

@Data
public class QuestionDisplayCommentDTO {
    //此条评论的id
    private Integer id;
    //评论的用户
    private String avatar;
    private String nickname;

    //评论时间
    private Long gmtCreate;
    //子评论数目
    private Integer commentCount;
    //内容
    private String content;
}