package com.yang.demo.dto;

import com.yang.demo.model.Question;
import com.yang.demo.model.User;
import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/14 17:46
 * Description:
 * 问题详情页
 * 包含一个问题 以及发布问题的用户信息
 */
@Data
public class QuestionDetailsDTO {
    private Question question;
    private User user;
}