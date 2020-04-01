package com.yang.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author Yiang37
 * @Date 2020/4/1 12:57
 * Description:
 */
@Controller
public class TempController {
    @GetMapping("/temp")
    public String toTemp(){
        return "temp";
    }
}