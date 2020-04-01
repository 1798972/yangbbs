package com.yang.demo.controller;

import com.yang.demo.dto.AskQuestionStateDTO;
import com.yang.demo.dto.QuestionEditDTO;
import com.yang.demo.enums.QuestionStateEnums;
import com.yang.demo.enums.SuccessOrErrorStateEnums;
import com.yang.demo.model.Question;
import com.yang.demo.model.User;
import com.yang.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Yiang37
 * @Date 2020/3/6 17:43
 * Description:
 * 提问页面
 */
@Controller
public class AskQuestionController {
    @Autowired
    private QuestionService questionService;

    //发布/编辑问题
    @PostMapping("/askQuestion")
    @ResponseBody
    public AskQuestionStateDTO askQuestion(@RequestParam(value = "questionId",required = false)String questionId,
                                           @RequestParam("title") String title,
                                           @RequestParam("description") String des,
                                           @RequestParam("tag") String tag,
                                           HttpServletRequest request) {

        AskQuestionStateDTO askQuestionStateDTO = new AskQuestionStateDTO();

        if (request.getSession().getAttribute("user") == null) {
            askQuestionStateDTO.setState(SuccessOrErrorStateEnums.ERROR.getState());
            askQuestionStateDTO.setErrorCode(QuestionStateEnums.USER_IS_UNLOGIN.getCode());
            askQuestionStateDTO.setDes(QuestionStateEnums.USER_IS_UNLOGIN.getDes());
            return askQuestionStateDTO;
        }
        if ("".equals(title) || title == null) {
            askQuestionStateDTO.setState(SuccessOrErrorStateEnums.ERROR.getState());
            askQuestionStateDTO.setErrorCode(QuestionStateEnums.TITLE_IS_ERROR.getCode());
            askQuestionStateDTO.setDes(QuestionStateEnums.TITLE_IS_ERROR.getDes());
            return askQuestionStateDTO;
        }
        if ("".equals(des) || des == null) {
            askQuestionStateDTO.setState(SuccessOrErrorStateEnums.ERROR.getState());
            askQuestionStateDTO.setErrorCode(QuestionStateEnums.DESCRIPTION_IS_ERROR.getCode());
            askQuestionStateDTO.setDes(QuestionStateEnums.DESCRIPTION_IS_ERROR.getDes());
            return askQuestionStateDTO;
        }
        //这个标签部分看看能不能改成服务器端 不然post过来的还是可以插入
        if ("".equals(tag) || tag == null) {
            askQuestionStateDTO.setState(SuccessOrErrorStateEnums.ERROR.getState());
            askQuestionStateDTO.setErrorCode(QuestionStateEnums.TAG_IS_ERROR.getCode());
            askQuestionStateDTO.setDes(QuestionStateEnums.TAG_IS_ERROR.getDes());
            return askQuestionStateDTO;
        }
        User sessionUser = (User) request.getSession().getAttribute("user");
        //检验通过 完成发布问题操作
        Question tempQuestion = new Question();
        if ("".equals(questionId) || questionId==null){
            tempQuestion.setId(0);
        }else {
        tempQuestion.setId(Integer.parseInt(questionId));
        }
        tempQuestion.setTitle(title);
        tempQuestion.setDescription(des);
        tempQuestion.setTag(tag);
        tempQuestion.setUserId(sessionUser.getId());

        //发布问题
        boolean flag = questionService.publishOrEditOneQuestion(tempQuestion);
        if (flag) {
            askQuestionStateDTO.setState(SuccessOrErrorStateEnums.SUCCESS.getState());
            askQuestionStateDTO.setErrorCode(QuestionStateEnums.SUCCESS_TO_PUBLISH.getCode());
            askQuestionStateDTO.setDes(QuestionStateEnums.SUCCESS_TO_PUBLISH.getDes());
            return askQuestionStateDTO;
        } else {
            askQuestionStateDTO.setState(SuccessOrErrorStateEnums.ERROR.getState());
            askQuestionStateDTO.setErrorCode(QuestionStateEnums.INSERT_ONE_QUESTION_ERROR.getCode());
            askQuestionStateDTO.setDes(QuestionStateEnums.INSERT_ONE_QUESTION_ERROR.getDes());
            return askQuestionStateDTO;
        }

    }

    @GetMapping("askQuestion")
    public String toAskQuestion(HttpServletRequest request) {
        if (request.getSession().getAttribute("user") != null) {
            return "askquestion";
        } else {
            //抛出异常
            return "redirect:/";
        }
    }
}