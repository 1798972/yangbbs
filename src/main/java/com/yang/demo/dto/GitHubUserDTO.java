package com.yang.demo.dto;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/2/27 23:02
 * Description:  GitHub用户
 */

@Data
public class GitHubUserDTO {
    //github标识
    private Long id;
    private String name;
    //用户头像
    private String avatarUrl;  //fastjson可以自动把avatar_url转换成驼峰型变量名 让其符合java命名规则
    private String bio;



}