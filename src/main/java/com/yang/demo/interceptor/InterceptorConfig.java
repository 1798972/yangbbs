package com.yang.demo.interceptor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Yiang37
 * @Date 2020/3/5 0:34
 * Description:
 *  拦截器 区分过滤器 这个拦截器是拦截指定请求 然后可以有选择的做了操作再放行
 * 拦截器的配置，主要配置项就两个，一个是指定拦截器，第二个是指定拦截的URL
 * 注解！！！！
 */

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    SetSessionInterceptor setSessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(setSessionInterceptor)
                .addPathPatterns("/**");
        //除了/login请求，其他都要到sessionInterceptor中执行
        //   registry.addInterceptor(sessionInterceptor).excludePathPatterns("/login");
    }
}