package com.yang.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author Yiang37
 * @Date 2020/4/2 22:06
 * Description:
 * 发现页面
 */

@Controller
public class FindController {

    @RequestMapping("/find")
    public String toFind(){
        return "find";
    }
}