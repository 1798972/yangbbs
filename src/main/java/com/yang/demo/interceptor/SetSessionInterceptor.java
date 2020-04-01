package com.yang.demo.interceptor;

import com.yang.demo.model.User;
import com.yang.demo.service.NoticeService;
import com.yang.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Yiang37
 * @Date 2020/3/5 0:38
 * Description:
 * 实现一个拦截器
 * 检查cookie中是否有token
 * 有则设置对应的session
 */
@Service
public class SetSessionInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;
    @Autowired
    NoticeService noticeService;

    //该拦截器在每次处理请求之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("yangtoken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    //3.根据token 找到user
                    User dbUser = userService.findUserByToken(token);
                    if (dbUser != null) {
                        //4.如果user存在 就在session中存入user
                        request.getSession().setAttribute("user", dbUser);

                        //放入未读消息数
                        Long unReadNumber = noticeService.findUnReadNumber(dbUser.getId());
                        request.getSession().setAttribute("unReadNumber",unReadNumber);
                    }
                    break;
                }
            }
        }
        //true 处理完继续执行
        return true;
    }
}