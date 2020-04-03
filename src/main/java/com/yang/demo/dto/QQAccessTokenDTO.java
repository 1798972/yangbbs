package com.yang.demo.dto;

import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/2/27 15:45
 * Description:
 * 解析出token时所需
 * client_id = appid
 * client_secret= appkey
 * redirect_uri= redirectURI
 * code = code
 */

@Data
public class QQAccessTokenDTO {
    private String appId;
    private String appKey;
    private String redirectURI;
    private String code;
}