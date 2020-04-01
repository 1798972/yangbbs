package com.yang.demo.controller;

import com.yang.demo.dto.*;
import com.yang.demo.model.Question;
import com.yang.demo.model.User;
import com.yang.demo.service.NoticeService;
import com.yang.demo.service.QuestionService;
import com.yang.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author Yiang37
 * @Date 2020/3/6 21:05
 * Description:
 */
@Controller
public class PersonalCenterController {

    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NoticeService noticeService;

    @GetMapping("/center/{action}")
    public String myQuestions(@PathVariable("action")String action,
                              @RequestParam(name = "page", defaultValue = "1",required = false) String page,
                              @RequestParam(name = "size", defaultValue = "10",required = false) String size,
                              HttpServletRequest request,
                              Model model){

        User sessionUser = (User)request.getSession().getAttribute("user");
        //我的资料
        if ("myInformation".equals(action)){
            MyInfomationDTO myInfomationDTO = userService.findUserInfomationByUerId(sessionUser.getId());

            model.addAttribute("myInfomationDTO",myInfomationDTO);
            model.addAttribute("action","myInformation");

            return "personalcenter";
        }
        //我的问题
        if ("myQuestion".equals(action)){
            //根据页码 与 大小 获取页面对象 即 问题+分页列表
            MyQuestionAndPageDTO myQuestionAndPageDTOList = questionService.findMyQuestionsAndPage(page, size,sessionUser.getId());

            model.addAttribute("myQuestionAndPageDTOList",myQuestionAndPageDTOList);
            model.addAttribute("action","myQuestion");
            return "personalcenter";
        }
        //我的通知
        if ("myNotice".equals(action)){

            MyNoticeAndPageDTO myNoticeAndPageDTO = noticeService.findMyNoticesAndPage(page, size,sessionUser.getId());
            model.addAttribute("myNoticeAndPageDTO",myNoticeAndPageDTO);
            model.addAttribute("action","myNotice");
            return "personalcenter";
        }

        return "redirect:/";
    }


    //退出登录
//    @RequestMapping(method = RequestMethod.GET, value = "/logout")
    @GetMapping("/logout")
    public String logOut(HttpServletRequest request,
                         HttpServletResponse response) {
        //1.移除session!!!!!!!!!
        request.getSession().removeAttribute("user");
        //2.移除cookie 替换掉token即可
        Cookie out = new Cookie("yangtoken", null);
        out.setMaxAge(0);
        response.addCookie(out);
        return "redirect:/";
    }
}