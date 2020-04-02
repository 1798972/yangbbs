package com.yang.demo.service;

import com.yang.demo.dto.*;
import com.yang.demo.enums.CommentTypeEnums;
import com.yang.demo.exception.CustomizeErrorCode;
import com.yang.demo.exception.CustomizeException;
import com.yang.demo.mapper.CommentMapper;
import com.yang.demo.mapper.QuestionMapper;
import com.yang.demo.mapper.UserMapper;
import com.yang.demo.model.Comment;
import com.yang.demo.model.Question;
import com.yang.demo.model.User;
import com.yang.demo.provider.HtmlProvider;
import com.yang.demo.provider.PageProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Yiang37
 * @Date 2020/3/7 21:34
 * Description:
 * 问题service层
 */

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private PageProvider pageProvider;
    @Autowired
    private HtmlProvider htmlProvider;

    //发布一个问题
    public boolean publishOrEditOneQuestion(Question tempQuestion) {
        //发布或者更新一个问题
        //id为空则是新问题
        if ("".equals(tempQuestion.getId() + "") || null == tempQuestion.getId() || 0 == tempQuestion.getId()) {
            tempQuestion.setGmtCreate(System.currentTimeMillis());
            tempQuestion.setGmtModified(System.currentTimeMillis());
            tempQuestion.setViewCount(0);
            tempQuestion.setLikeCount(0);
            tempQuestion.setCommentCount(0);
            Integer row = questionMapper.insertOneQuestion(tempQuestion);
            return row == 1;
        } else {
            //更新
            tempQuestion.setGmtModified(System.currentTimeMillis());
            Integer row = questionMapper.updateOneQuestion(tempQuestion);
            return row == 1;
        }
    }


    //根据页码和数目 获取问题+分页
    /*public IndexQuestionAndPageDTO findIndexQuestionsAndPage(String page, String size) {
        //需要先计算总条数
        String tatalCount = questionMapper.findQuestionTotalCount();
        //矫正页码 为啥要写这个 -1可以直接设置为1 但是最后一页得计算
        //由于计算总页数要size 所以size得先矫正下
        String checkedSize = pageProvider.checkSizeNow(size);
        String checkedPage = pageProvider.checkPageNow(page, tatalCount, checkedSize);

        //1.获取问题列表
        //计算开始数
        String startNum = pageProvider.startNum(checkedPage, checkedSize);
        List<IndexQuestionDTO> indexQuestionDTOList = new ArrayList<>();
        //得到问题列表并做好转换
        List<Question> dbQuestionList = questionMapper.findQuestionList(startNum, checkedSize);
        for (Question question : dbQuestionList) {
            User dbUser = userMapper.findUserById(question.getUserId());
            IndexQuestionDTO indexQuestionDTO = new IndexQuestionDTO();

            indexQuestionDTO.setId(question.getId());
            indexQuestionDTO.setUserIcon(dbUser.getAvatar());
            indexQuestionDTO.setTitle(question.getTitle());
            //html转普通文本
            String resStr = htmlProvider.getTextFromHtml(question.getDescription());
            indexQuestionDTO.setDescription(resStr);

            indexQuestionDTO.setGmtCreate(question.getGmtCreate());
            indexQuestionDTOList.add(indexQuestionDTO);
        }


        //2.获取页码数据 //也要矫正页码和大小
        PageDTO pageDTO = pageProvider.gainPageDTO(checkedPage, checkedSize, tatalCount, 3);

        //设置页面对象
        IndexQuestionAndPageDTO indexQuestionAndPageDTO = new IndexQuestionAndPageDTO();
        indexQuestionAndPageDTO.setIndexQuestionDTOList(indexQuestionDTOList);
        indexQuestionAndPageDTO.setPageDTO(pageDTO);

        return indexQuestionAndPageDTO;
    }*/

    //获取问题详情页的数据对象
    public QuestionDetailsDTO findQuestionDetails(String questionId) {
        //获取到问题的id 查询出该问题 并在页面上显示
        Question dbQuestion = questionMapper.findQuestionByQuestionId(questionId);
        if (dbQuestion == null){
            throw new CustomizeException(CustomizeErrorCode.QUESRION_NOT_EXIST);
        }
        //问题的浏览数+1
        questionMapper.increaseOneViewCount(questionId);
        //浏览数增加后再次查询改问题
        dbQuestion = questionMapper.findQuestionByQuestionId(questionId);

        User dbUser = userMapper.findUserById(dbQuestion.getUserId());
        QuestionDetailsDTO questionDetailsDTO = new QuestionDetailsDTO();

        questionDetailsDTO.setQuestion(dbQuestion);
        questionDetailsDTO.setUser(dbUser);


        return questionDetailsDTO;
    }

    //先检查发布者和session中的用户是不是同一个
    //任意一个为空都返回false
    public boolean checkCreateQuestionUserAndSessionUser(HttpServletRequest request,
                                                         String questionId) {
        User sessionUser = (User) request.getSession().getAttribute("user");
        Integer userId = questionMapper.findUserIdByQuestionId(questionId);
        User dbUser = userMapper.findUserById(userId);
        if (sessionUser != null && dbUser != null) {
            return sessionUser.getId().equals(dbUser.getId());
        }
        return false;
    }

    //修改问题时 返回信息
    public QuestionEditDTO findQuestionWhenEdit(String questionId) {
        Question dbQuestion = questionMapper.findQuestionByQuestionId(questionId);
        QuestionEditDTO questionEditDTO = new QuestionEditDTO();
        questionEditDTO.setId(dbQuestion.getId());
        questionEditDTO.setTitle(dbQuestion.getTitle());
        questionEditDTO.setDescription(dbQuestion.getDescription());
        questionEditDTO.setTag(dbQuestion.getTag());
        return questionEditDTO;
    }

    //删除一个问题
    public boolean deleteOneQuestion(String questionId) {
       Integer row = questionMapper.deleteQuestionByQuestionId(questionId);
        return row == 1;
    }

    //根据问题id 找到问题下的评论信息列表
    public List<QuestionDisplayCommentDTO> findQuestionCommentList(String qid, Integer rows) {

        List<QuestionDisplayCommentDTO> questionDisplayCommentDTOList = new ArrayList<>();

        List<Comment> dbCommentList = commentMapper.findCommentByParentId(qid,CommentTypeEnums.QUESTION_COMMENT.getType(),rows);
        //得到问题列表后 找到每条评论的用户
        for (Comment tempComment : dbCommentList){
            QuestionDisplayCommentDTO questionDisplayCommentDTO = new QuestionDisplayCommentDTO();
            User dbUser = userMapper.findUserById(tempComment.getUserId());

            questionDisplayCommentDTO.setId(tempComment.getId());
            questionDisplayCommentDTO.setAvatar(dbUser.getAvatar());
            questionDisplayCommentDTO.setNickname(dbUser.getNickname());
            questionDisplayCommentDTO.setGmtCreate(tempComment.getGmtCreate());
            questionDisplayCommentDTO.setCommentCount(tempComment.getCommentCount());
            questionDisplayCommentDTO.setContent(tempComment.getContent());

            questionDisplayCommentDTOList.add(questionDisplayCommentDTO);
        }
        return questionDisplayCommentDTOList;
    }


    //分页信息获取我的问题列表
    public MyQuestionAndPageDTO findMyQuestionsAndPage(String page, String size, Integer userId) {
        //需要先计算总条数
        String tatalCount = questionMapper.findQuestionTotalCountByUserId(userId);
        //矫正页码 为啥要写这个 -1可以直接设置为1 但是最后一页得计算
        //由于计算总页数要size 所以size得先矫正下
        String checkedSize = pageProvider.checkSizeNow(size);
        String checkedPage = pageProvider.checkPageNow(page, tatalCount, checkedSize);
        //page与size范围矫正好了 再进行使用

        //1.获取问题列表
        //计算开始数
        String startNum = pageProvider.startNum(checkedPage, checkedSize);
        List<MyQuestionDTO> myQuestionDTOList = new ArrayList<>();
        //得到问题列表并做好转换
        List<Question> dbQuestionList = questionMapper.findQuestionListByUserId(startNum, checkedSize, userId);
        for (Question question : dbQuestionList) {
            MyQuestionDTO myQuestionDTO = new MyQuestionDTO();
            myQuestionDTO.setId(question.getId());
            myQuestionDTO.setTitle(question.getTitle());
            myQuestionDTO.setGmtCreate(question.getGmtCreate());
            myQuestionDTOList.add(myQuestionDTO);
        }

        //2.获取页码数据 //也要矫正页码和大小
        PageDTO pageDTO = pageProvider.gainPageDTO(checkedPage, checkedSize, tatalCount, 3);
        //设置页面对象
        MyQuestionAndPageDTO myQuestionAndPageDTO = new MyQuestionAndPageDTO();
        myQuestionAndPageDTO.setMyQuestionDTOList(myQuestionDTOList);
        myQuestionAndPageDTO.setPageDTO(pageDTO);

        return myQuestionAndPageDTO;
    }

    //查找相关问题的列表
    public List<AboutQuestionDTO> findAboutQuestionList(String questionId) {

        //根据问题id找到所有的标签
        Question targetQuestion = questionMapper.findQuestionByQuestionId(questionId);
        String tags = targetQuestion.getTag();
        String regTag = tags.replace(',','|');
        //根据标签查找到相关的问题
        List<Question> aboutQuestions = questionMapper.findAboutQuestions(targetQuestion.getId(),regTag);

        List<AboutQuestionDTO> aboutQuestionDTOList = new ArrayList<>();
        for (Question aboutQuestion : aboutQuestions) {
            AboutQuestionDTO aboutQuestionDTO = new AboutQuestionDTO();
            aboutQuestionDTO.setId(aboutQuestion.getId());
            aboutQuestionDTO.setTitle(aboutQuestion.getTitle());
            aboutQuestionDTOList.add(aboutQuestionDTO);
        }

        if (aboutQuestionDTOList.size()<=10){
            return aboutQuestionDTOList;
        }else {
            //只保留前十个
        return aboutQuestionDTOList.subList(0,10);
        }
    }
    //根据关键词查找相关问题
    public IndexQuestionAndPageDTO findSearchQuestionsAndPage(String page, String size, String keyWord) {

        String tatalCount;
        List<Question> dbQuestionList = new ArrayList<>();
        if (keyWord == null || "".equals(keyWord)){
            tatalCount = questionMapper.findQuestionTotalCount();
        }else {
            //空格转换成|
            String regsearch = keyWord.replace(' ','|');
            tatalCount = questionMapper.selectQuestionQueryCounts(regsearch);
        }

        String checkedSize = pageProvider.checkSizeNow(size);
        String checkedPage = pageProvider.checkPageNow(page, tatalCount, checkedSize);
        String startNum = pageProvider.startNum(checkedPage, checkedSize);
        List<IndexQuestionDTO> indexQuestionDTOList = new ArrayList<>();

        //得到问题列表并做好转换
        if (keyWord == null || "".equals(keyWord)){
            dbQuestionList = questionMapper.findQuestionList(startNum, checkedSize);
        }else {
            //空格转换成|
            String regsearch = keyWord.replace(' ','|');
            //查询问题总条数
            dbQuestionList = questionMapper.findQuestionQueryList(startNum, checkedSize, regsearch);
        }

        for (Question question : dbQuestionList) {
            User dbUser = userMapper.findUserById(question.getUserId());
            IndexQuestionDTO indexQuestionDTO = new IndexQuestionDTO();

            indexQuestionDTO.setId(question.getId());
            indexQuestionDTO.setUserIcon(dbUser.getAvatar());
            indexQuestionDTO.setTitle(question.getTitle());
            //html转普通文本
            String resStr = htmlProvider.getTextFromHtml(question.getDescription());
            indexQuestionDTO.setDescription(resStr);

            indexQuestionDTO.setGmtCreate(question.getGmtCreate());
            indexQuestionDTOList.add(indexQuestionDTO);
        }


        //2.获取页码数据 //也要矫正页码和大小
        PageDTO pageDTO = pageProvider.gainPageDTO(checkedPage, checkedSize, tatalCount, 3);

        //设置页面对象
        IndexQuestionAndPageDTO indexQuestionAndPageDTO = new IndexQuestionAndPageDTO();
        indexQuestionAndPageDTO.setIndexQuestionDTOList(indexQuestionDTOList);
        indexQuestionAndPageDTO.setPageDTO(pageDTO);

        return indexQuestionAndPageDTO;
    }
}