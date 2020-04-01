package com.yang.demo.controller;

import com.yang.demo.dto.MyNoticeDTO;
import com.yang.demo.enums.CommentTypeEnums;
import com.yang.demo.model.User;
import com.yang.demo.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Yiang37
 * @Date 2020/3/25 15:50
 * Description:
 * 在通知中点击问题时 不能直接跳转 要先经过这个
 */

@Controller
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @GetMapping("/noticeToQuestion")
    public String noticeToQuestion(HttpServletRequest request,
                                   @RequestParam("noticeId")String noticeId,
                                   @RequestParam("clickId")String cilckId) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        //读取操作
         noticeService.readOneNotification(Integer.parseInt(noticeId), user);

         return "redirect:/question?id="+cilckId;
    }

    @PostMapping("/session/getUnReadNumber")
    @ResponseBody
    public Long getUnReadNumber(HttpServletRequest request){
        Long unReadNumber = (Long) request.getSession().getAttribute("unReadNumber");
        return unReadNumber;
    }
}