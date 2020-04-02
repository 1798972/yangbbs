package com.yang.demo.controller;

import com.yang.demo.dto.*;
import com.yang.demo.enums.CommentStateEnums;
import com.yang.demo.enums.SuccessOrErrorStateEnums;
import com.yang.demo.mapper.CommentMapper;
import com.yang.demo.mapper.QuestionMapper;
import com.yang.demo.model.User;
import com.yang.demo.provider.RedisProvider;
import com.yang.demo.service.CommentService;
import com.yang.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author Yiang37
 * @Date 2020/3/14 15:18
 * Description:
 * 问题详情的controller
 */

@Controller
public class QuestionDetailsController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisProvider redisProvider;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;

    //进入问题详情
    @GetMapping("/question")
    public String toQuestionDetails(@RequestParam("id") String questionId,
                                    Model model) {
        QuestionDetailsDTO questionDetailsDTO = questionService.findQuestionDetails(questionId);
        List<AboutQuestionDTO> aboutQuestionDTOList = questionService.findAboutQuestionList(questionId);


        model.addAttribute("questionDetailsDTO", questionDetailsDTO);
        model.addAttribute("aboutQuestionDTOList",aboutQuestionDTOList);
        return "questiondetails";
    }

    //编辑问题
    @GetMapping("/askQuestion/edit")
    public String editQuestion(@RequestParam("id") String questionId,
                               HttpServletRequest request,
                               Model model) {
        //先检查发布者和session中的用户是不是同一个
        if (!questionService.checkCreateQuestionUserAndSessionUser(request, questionId)) {
            return "redirect:/";
        }

        //重新编辑时 需要问题标题 问题内容 问题标签
        QuestionEditDTO questionEditDTO = questionService.findQuestionWhenEdit(questionId);
        model.addAttribute("questionId", questionEditDTO.getId());
        model.addAttribute("title", questionEditDTO.getTitle());
        model.addAttribute("description", questionEditDTO.getDescription());
        model.addAttribute("tag", questionEditDTO.getTag());
//        model.addAttribute("questionEditDTO",questionEditDTO);
        return "askquestion";
    }

    //删除问题
    @PostMapping("/question/delete")
    @ResponseBody
    public String deleteOneQuestion(@RequestParam("questionId") String questionId) {

        boolean flag = questionService.deleteOneQuestion(questionId);
        if (flag) {
            return "success";
        } else {
            return "error";
        }
    }

    //查找点赞总数
    @PostMapping("/question/thumbCount")
    @ResponseBody
    public Long getThumbCount(@RequestParam("qid") String qid) {
        String redisKey = "qid:thumb:" + qid;
        return redisProvider.bitCount(redisKey);
    }

    //获取点赞状态
    @PostMapping("/question/getThumbStatu")
    @ResponseBody
    public boolean getThumbStatu(@RequestParam("qid") String qid,
                                 @RequestParam("uid") String uid) {
        if (uid == null || "".equals(uid)) {
            uid = 0 + "";
        }
        String redisKey = "qid:thumb:" + qid;
        Integer redisOffset = Integer.parseInt(uid);
        return redisProvider.getBit(redisKey, redisOffset);
    }

    //点赞操作
    @PostMapping("/question/giveThumb")
    @ResponseBody
    public Long giveOneThumb(@RequestParam("qid") String qid,
                             @RequestParam("uid") String uid) {

        //得到问题的id和当前用户的id
        //点赞操作
        String redisKey = "qid:thumb:" + qid;
        Long redisOffset = Long.parseLong(uid);
        boolean dbFlag = redisProvider.setBit(redisKey, redisOffset, true);
        //查总人数
        Long thumbCount = redisProvider.bitCount(redisKey);
//        System.out.println("setbit "+redisKey+" "+redisOffset+" "+true);
//        System.out.println(dbFlag);
        //为false则插入成功
        if (!dbFlag) {
            return thumbCount;
        } else {
            return -1L;
        }
    }

    //取消点赞操作
    @PostMapping("/question/cancelThumb")
    @ResponseBody
    public Long cancelOneThumb(@RequestParam("qid") String qid,
                               @RequestParam("uid") String uid) {

        //得到问题的id和当前用户的id
        //点赞操作
        String redisKey = "qid:thumb:" + qid;
        Long redisOffset = Long.parseLong(uid);
        boolean dbFlag = redisProvider.setBit(redisKey, redisOffset, false);
        Long thumbCount = redisProvider.bitCount(redisKey);
        //为true则插入0成功
        if (dbFlag) {
            return thumbCount;
        } else {
            return -1L;
        }
    }

    //评论一个问题
    @PostMapping("/question/comment")
    @ResponseBody
    public CommentStateDTO commentOneQuestion(@ModelAttribute CommentDTO commentDTO,
                                              HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("user");
        CommentStateDTO commentStateDTO = new CommentStateDTO();
        //内容为空
        if ("".equals(commentDTO.getContent())) {
            commentStateDTO.setState(SuccessOrErrorStateEnums.ERROR.getState());
            commentStateDTO.setCode(CommentStateEnums.CONTENT_IS_NULL.getCode());
            commentStateDTO.setDes(CommentStateEnums.CONTENT_IS_NULL.getDes());
            return commentStateDTO;
        }
        if (commentDTO.getParentId() == null) {
            commentStateDTO.setState(SuccessOrErrorStateEnums.ERROR.getState());
            commentStateDTO.setCode(CommentStateEnums.QUESTION_IS_NULL.getCode());
            commentStateDTO.setDes(CommentStateEnums.QUESTION_IS_NULL.getDes());
            return commentStateDTO;
        }
        //用户未登录
        if (sessionUser == null || commentDTO.getUserId() == null) {
            commentStateDTO.setState(SuccessOrErrorStateEnums.ERROR.getState());
            commentStateDTO.setCode(CommentStateEnums.USER_IS_NULL.getCode());
            commentStateDTO.setDes(CommentStateEnums.USER_IS_NULL.getDes());
            return commentStateDTO;
        }
        //seeion用户 和前端传递的session用户id 不匹配
        if (!sessionUser.getId().equals(commentDTO.getUserId())) {
            commentStateDTO.setState(SuccessOrErrorStateEnums.ERROR.getState());
            commentStateDTO.setCode(CommentStateEnums.USER_IS_ERROR.getCode());
            commentStateDTO.setDes(CommentStateEnums.USER_IS_ERROR.getDes());
            return commentStateDTO;
        }

        //验证通过 进行回复问题操作
        boolean inserFlag = commentService.commentOneQuestionOrComment(commentDTO);

        if (inserFlag) {
            commentStateDTO.setState(SuccessOrErrorStateEnums.SUCCESS.getState());
            commentStateDTO.setCode(CommentStateEnums.COMMENT_QUESTION_SUCCESS.getCode());
            commentStateDTO.setDes(CommentStateEnums.COMMENT_QUESTION_SUCCESS.getDes());
            return commentStateDTO;
        } else {
            commentStateDTO.setState(SuccessOrErrorStateEnums.ERROR.getState());
            commentStateDTO.setCode(CommentStateEnums.INSERT_QUESTION_COMMENT_ERROR.getCode());
            commentStateDTO.setDes(CommentStateEnums.INSERT_QUESTION_COMMENT_ERROR.getDes());
            return commentStateDTO;
        }
    }

    //获取问题的评论数
    @PostMapping("/question/getQuestionCommentCount")
    @ResponseBody
    public Integer QuestionCommentCount(@RequestParam("qid") String qid) {
        Integer res = questionMapper.findCommentCountByQuestionId(qid);
        if (res == null) {
            return 0;
        }
        return res;
    }

    //获取评论信息的列表
    @PostMapping("/question/getQuestionCommentList")
    @ResponseBody
    public List<QuestionDisplayCommentDTO> getQuestionCommentList(@RequestBody Map map) {
        String qid = (String) map.get("qid");
        Integer rows = (Integer) map.get("rows");
        List<QuestionDisplayCommentDTO> questionDisplayCommentDTOList = questionService.findQuestionCommentList(qid, rows);

        return questionDisplayCommentDTOList;
    }

    //获取评论总数
    @PostMapping("/question/getCommentCommentCount")
    @ResponseBody
    public Integer CommentCommentCount(@RequestParam("cid") String cid) {
        Integer res = commentMapper.findCommentCountByCommentId(cid);
        if (res == null) {
            return 0;
        }
        return res;
    }

    //获取评论下的二级评论
    @PostMapping("/question/getCommentCommentList")
    @ResponseBody
    public List<Display2CommentDTO>  getCommentCommentList(@RequestBody Map map){
        String parentId = (String)map.get("cid");
        Integer rows = (Integer) map.get("rows");


        List<Display2CommentDTO> display2CommentDTOList = new ArrayList<>();
        display2CommentDTOList = commentService.find2rdCommentList(parentId,rows);

        return display2CommentDTOList;
    }
}