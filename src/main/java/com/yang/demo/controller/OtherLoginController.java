package com.yang.demo.controller;

import com.yang.demo.dto.GitHubAccessTokenDTO;
import com.yang.demo.dto.GitHubUserDTO;
import com.yang.demo.dto.QQAccessTokenDTO;
import com.yang.demo.dto.QQUserDTO;
import com.yang.demo.enums.UserTypeEnums;
import com.yang.demo.model.User;
import com.yang.demo.model.UserAuth;
import com.yang.demo.provider.GitHubProvider;
import com.yang.demo.provider.QQProvider;
import com.yang.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Author Yiang37
 * @Date 2020/1/22 12:11
 * Description:
 * 第三方登录
 * <p>
 * 区别于普通用户
 * 使用QQ等第三方直接登录时
 * 要判断以前有没有登录过 登录过就是更新信息 没登陆过就要创建一个新用户 所以就可能产生一个顺便注册的步骤
 * 而普通登录就不包含这个验证老用户的过程 直接检查密码就行了
 */

@Slf4j
@Controller
public class OtherLoginController {
    @Autowired
    private QQProvider qqProvider;
    @Autowired
    private GitHubProvider gitHubProvider;
    @Autowired
    private UserService userService;

    //QQ相关
    @Value("${qq.appId}")
    private String qqAppId;
    @Value("${qq.appKey}")
    private String qqAppKey;
    @Value("${qq.redirect.uri}")
    private String qqRedirectURI;
    //GitHub相关
    //application.properties文件中的属性值进行注入
    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;

    //1.QQ登录
    @GetMapping("/qqcallback")
    public String qqLogin(@RequestParam("code") String code,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        QQAccessTokenDTO qqAccessTokenDTO = new QQAccessTokenDTO();
        qqAccessTokenDTO.setAppId(qqAppId);
        qqAccessTokenDTO.setAppKey(qqAppKey);
        qqAccessTokenDTO.setRedirectURI(qqRedirectURI);
        qqAccessTokenDTO.setCode(code);
        //2.1利用返回的code值  FD190E1C0EE4C6897D7040E52AF1CEDA  //获取token
        String qqToken = qqProvider.getQQToken(qqAccessTokenDTO);
        //2.2 利用返回的token值 获取OpenID
        String openID = qqProvider.getQQOpenId(qqToken);
        //2.3 利用返回的openId值 获取用户信息
        QQUserDTO qqUserDTO = qqProvider.getQQUserDTO(qqToken, qqAppId, openID);
        //设置上附加的openID

        //如果不为空 则存在该QQ用户
        if (qqUserDTO != null) {
            //插入或者新增这个QQ用户
            String token = UUID.randomUUID().toString();
            UserAuth tempUserAuth = new UserAuth();
            User tempUser = new User();
            tempUser.setAvatar(qqUserDTO.getFigureurlQQ1());
            tempUser.setNickname(qqUserDTO.getNickname());
            tempUser.setToken(token);
            tempUserAuth.setVerification(qqUserDTO.getQqOpenId());
            tempUserAuth.setUType(UserTypeEnums.QQUser.getUserType());

            //插入或者更新用户信息
            userService.insertOrUpdateUserInfo(tempUser, tempUserAuth);
            //cookie中插入token标记
            response.addCookie(new Cookie("yangtoken", token));
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }

    //2.GitHub登录
    //点击登录链接之后会回传一个地址
    //      https://localhost:8089/callback?code=ec043f38116846104634&state=1
    @GetMapping("/githubcallback")
    public String gitHubLogin(@RequestParam(name = "code") String code,
                              @RequestParam(name = "state") String state,
                              HttpServletResponse response) {
        //对回传的地址进行处理
        GitHubAccessTokenDTO accessTokenDTO = new GitHubAccessTokenDTO();

        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        //得到用户的token
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        //通过token得到用户
        GitHubUserDTO gitHubUser = gitHubProvider.getUser(accessToken);

        if (gitHubUser != null) {
            UserAuth tempUserAuth = new UserAuth();
            User tempUser = new User();
            String token = UUID.randomUUID().toString();

            tempUser.setToken(token);
            tempUser.setNickname(gitHubUser.getName());
            tempUser.setAvatar(gitHubUser.getAvatarUrl());

            tempUserAuth.setUType(UserTypeEnums.GitHubUser.getUserType());
            tempUserAuth.setVerification(String.valueOf(gitHubUser.getId()));
            //插入或者更新用户信息
            userService.insertOrUpdateUserInfo(tempUser, tempUserAuth);
            response.addCookie(new Cookie("yangtoken", token));
            return "redirect:/";
        } else {
//            追加到日志中 lombok的@Slf4j注解
            log.error("github login error,{}", gitHubUser);
            return "redirect:/";
        }
    }
}
