package com.yang.demo.dto;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/24 16:21
 * Description:
 * 个人中心-我的资料
 * //    头像
 * //            昵称
 * //    羊羊号
 * //            手机号
 * //    QQ号
 * //            GitHub账号
 * //    注册时间
 */

@Data
public class MyInfomationDTO {
    private Integer id;
    private String avatar;
    private String nickname;
    private String yyNumber;
    private String telNumber;
    private String qqNumber;
    private String githubNumber;
    private Long gmtCreate;
}