package com.yang.demo.dto;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/25 14:45
 * Description:
 * //xx 回复了你的 问题/评论 问题/评论的内容
 */

@Data
public class MyNoticeDTO {
    private Integer id;
    private Integer outerId;
    private Integer clickId;
    private String creatorName;
    private Integer ntype;
    private String outerTitle;
    private Integer nstate;
    private Long gmtCreate;
}