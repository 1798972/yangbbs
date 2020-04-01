package com.yang.demo.controller;

import com.yang.demo.provider.RedisProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Yiang37
 * @Date 2020/3/16 18:32
 * Description:
 *
 * Redis测试类
 * 暂时没有用
 */

@Controller
public class RedisController {

    @Autowired
    RedisProvider redisProvider;

    @RequestMapping("/redis/{key}")
    @ResponseBody
    public String getRedisKey(@PathVariable("key")String key,
                              Model model){


        redisProvider.set(key,"199899");
        redisProvider.del(key);

        Object str = redisProvider.get(key);

        return str.toString();
    }
}