package com.yang.demo.controller;

import com.yang.demo.dto.IndexQuestionAndPageDTO;
import com.yang.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author Yiang37
 * @Date 2020/2/27 0:03
 * Description:
 */
@Controller
public class IndexController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/")
    public String showIndex(@RequestParam(value = "page", defaultValue = "1",required = false) String page,
                            @RequestParam(value = "size", defaultValue = "5",required = false) String size,
                            @RequestParam(value = "keyWord",required = false)String keyWord,
                            Model model) {

        //！！！keyWord带*会报错
        if (keyWord==null){
            keyWord = "";
        }

        //非法字符的处理 有的话直接删除了 免得sql异常
        //+ ) (  ?
        String errStr = "*+?(){}[";
        keyWord = replaceAllErrorChar(keyWord,errStr);

        //根据页码 与 大小 获取页面对象 即 问题+分页列表
        IndexQuestionAndPageDTO indexQuestionAndPageDTO = questionService.findSearchQuestionsAndPage(page,size,keyWord);

        model.addAttribute("keyWord",keyWord);
        model.addAttribute("indexQuestionAndPageDTO",indexQuestionAndPageDTO);
        return "index";
    }

    //删除掉非法的字符 返回一个合理的字符串
    private String replaceAllErrorChar(String sourceStr, String errStr){
        List<String> resStrList = new LinkedList<>();
        for (int i = 0;i<sourceStr.length();i++){
            String c = sourceStr.substring(i,i+1);
            if (!errStr.contains(c)){
                resStrList.add(c);
            }
        }
        String resStr = "";
        for (String s : resStrList) {
            resStr += s;
        }
        return resStr;
    }
}