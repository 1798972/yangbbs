package com.yang.demo.provider;

import com.alibaba.fastjson.JSON;
import com.yang.demo.dto.GitHubAccessTokenDTO;
import com.yang.demo.dto.GitHubUserDTO;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class GitHubProvider {
    //1.得到用户token
    public String getAccessToken(GitHubAccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        //RequestBody对象转换成json对象
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //2.根据token得到对应的github用户
    public GitHubUserDTO getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)  //这个网址不要写错了
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
//            System.out.println(string);
            //string json格式解析成gitHubUser对象
            GitHubUserDTO gitHubUser = JSON.parseObject(string, GitHubUserDTO.class);
            return gitHubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
