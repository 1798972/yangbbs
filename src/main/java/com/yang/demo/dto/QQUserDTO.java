package com.yang.demo.dto;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/2/27 17:00
 * Description: 登录时用于接收登录信息  临时QQ用户信息
 */

@Data
public class QQUserDTO {
    //1.昵称
    private String nickname;
    //2.头像figureurl_qq_1
    private String figureurlQQ1;
    //3.接入网站唯一标识 openId
    private String qqOpenId;
}