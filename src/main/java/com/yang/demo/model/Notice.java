package com.yang.demo.model;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/25 14:01
 * Description:
 */
@Data
public class Notice {
    private Integer id;
    private Integer ntype;
    private Integer creatorId;
    private String creatorName;
    private Integer outerId;
    private String outerTitle;
    private Integer receiverId;
    private Integer nstate;
    private Long gmtCreate;
}