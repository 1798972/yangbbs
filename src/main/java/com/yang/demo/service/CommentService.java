package com.yang.demo.service;

import com.yang.demo.dto.CommentDTO;
import com.yang.demo.dto.Display2CommentDTO;
import com.yang.demo.enums.CommentTypeEnums;
import com.yang.demo.enums.NoticeStateEnums;
import com.yang.demo.mapper.CommentMapper;
import com.yang.demo.mapper.NoticeMapper;
import com.yang.demo.mapper.QuestionMapper;
import com.yang.demo.mapper.UserMapper;
import com.yang.demo.model.Comment;
import com.yang.demo.model.Notice;
import com.yang.demo.model.Question;
import com.yang.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Yiang37
 * @Date 2020/3/17 14:15
 * Description:
 */

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NoticeMapper noticeMapper;


    @Transactional
    public boolean commentOneQuestionOrComment(CommentDTO commentDTO) {

        Comment dbComment = new Comment();
        dbComment.setCtype(commentDTO.getType());
        dbComment.setParentId(commentDTO.getParentId());
        dbComment.setUserId(commentDTO.getUserId());
        dbComment.setContent(commentDTO.getContent());
        dbComment.setLikeCount(0);
        dbComment.setCommentCount(0);
        dbComment.setGmtCreate(System.currentTimeMillis());
        dbComment.setGmtModified(System.currentTimeMillis());

        //评论表插入评论
        int crows = commentMapper.inserOneComment(dbComment);

        //问题表/评论表对应的评论数加1
        if (commentDTO.getType() == CommentTypeEnums.QUESTION_COMMENT.getType()) {
            //评论的是问题
            questionMapper.addOneQuestionCommentCount(commentDTO.getParentId());

            //创建通知信息
            Notice notice = new Notice();
            notice.setNtype(CommentTypeEnums.QUESTION_COMMENT.getType());
            notice.setCreatorId(commentDTO.getUserId());
            String userName = userMapper.findUserNameByUserId(commentDTO.getUserId());
            notice.setCreatorName(userName);
            //查找到评论对应的问题
            Question tempQuestion = questionMapper.findQuestionByQuestionId(commentDTO.getParentId()+"");
            //问题发起者的id
            notice.setReceiverId(tempQuestion.getUserId());
            notice.setOuterId(tempQuestion.getId());
            notice.setOuterTitle(tempQuestion.getTitle());
            notice.setNstate(NoticeStateEnums.UN_READ.getState());
            notice.setGmtCreate(System.currentTimeMillis());

            //插入评论
            noticeMapper.insertOneNotice(notice);
        }
        if (commentDTO.getType() == CommentTypeEnums.COMMENT_COMMENT.getType()) {
            //评论的是评论
            commentMapper.addOneCommentCommnetCount(commentDTO.getParentId());

            //创建通知信息
            Notice notice = new Notice();
            notice.setNtype(CommentTypeEnums.COMMENT_COMMENT.getType());
            notice.setCreatorId(commentDTO.getUserId());
            String userName = userMapper.findUserNameByUserId(commentDTO.getUserId());
            notice.setCreatorName(userName);
            //查找到评论对应的评论
            Comment tempComment = commentMapper.findCommentByCommentId(commentDTO.getParentId());
            notice.setReceiverId(tempComment.getUserId());
            notice.setOuterId(tempComment.getId());
            notice.setOuterTitle(tempComment.getContent());
            notice.setNstate(NoticeStateEnums.UN_READ.getState());
            notice.setGmtCreate(System.currentTimeMillis());

            //插入评论
            noticeMapper.insertOneNotice(notice);
        }

        return crows == 1;
    }

    //根据评论id查找对应的二级评论列表
    public List<Display2CommentDTO> find2rdCommentList(String parentId,Integer rows) {
        List<Comment> dbCommentList = commentMapper.findCommentByParentId(parentId, 1, rows);
        List<Display2CommentDTO> display2CommentDTOList = new ArrayList<>();
        for (Comment tempComment : dbCommentList) {
            User dbUser = userMapper.findUserById(tempComment.getUserId());
            Display2CommentDTO display2CommentDTO = new Display2CommentDTO();

            display2CommentDTO.setAvatar(dbUser.getAvatar());
            display2CommentDTO.setNickname(dbUser.getNickname());
            display2CommentDTO.setGmtCreate(tempComment.getGmtCreate());
            display2CommentDTO.setContent(tempComment.getContent());
            display2CommentDTO.setPid(parentId);

            display2CommentDTOList.add(display2CommentDTO);
        }
        return display2CommentDTOList;
    }
}