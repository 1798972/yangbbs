package com.yang.demo.provider;

import com.alibaba.fastjson.JSON;
import com.yang.demo.dto.QQAccessTokenDTO;
import com.yang.demo.dto.QQUserDTO;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author Yiang37
 * @Date 2020/2/27 15:13
 * Description:
 * 利用okhttp
 * 解析QQ用户的登录请求
 */

@Component
public class QQProvider {

    //1.得到QQ用户的token 传入code值
    // 成功返回token值 失败返回null
    public String getQQToken(QQAccessTokenDTO accessTokenDTO) {
        String appid = accessTokenDTO.getAppId();
        String appkey = accessTokenDTO.getAppKey();
        String redirectURI = accessTokenDTO.getRedirectURI();
        String code = accessTokenDTO.getCode();

        String asStr = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=" + appid + "&client_secret=" + appkey + "&redirect_uri=" + redirectURI + "&code=" + code;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(asStr)  //这个网址不要写错了
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
//              access_token=F72B107675F7A78C157F4509C86E320E&expires_in=7776000&refresh_token=ED29C1505D78251D4A897B5D2C10E357
            String qqtoken = string.split("&")[0].split("=")[1];
            return qqtoken;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //2.根据传入的token获取openid值
    // 成功返回openId值 失败返回null
    public String getQQOpenId(String qqToken) {
        String asStr = "https://graph.qq.com/oauth2.0/me?access_token=" + qqToken;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(asStr)  //这个网址不要写错了
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            //callback( {"client_id":"101838917","openid":"E70AF3616B2163031C52093AB2D66E91"} );
            String openId = string.split("\"")[7];
            return openId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //3.根据openID获取用户信息
    public QQUserDTO getQQUserDTO(String qqToken, String qqAppId, String openID) {
        String asStr = "https://graph.qq.com/user/get_user_info?access_token=" + qqToken + "&oauth_consumer_key=" + qqAppId + "&openid=" + openID;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(asStr)  //这个网址不要写错了
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            //callback( {"client_id":"101838917","openid":"E70AF3616B2163031C52093AB2D66E91"} );
            QQUserDTO qqUserDTO =JSON.parseObject(string, QQUserDTO.class);
            qqUserDTO.setQqOpenId(openID);

            return qqUserDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}