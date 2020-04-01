package com.yang.demo.service;

import com.yang.demo.dto.*;
import com.yang.demo.enums.CommentTypeEnums;
import com.yang.demo.enums.NoticeStateEnums;
import com.yang.demo.mapper.CommentMapper;
import com.yang.demo.mapper.NoticeMapper;
import com.yang.demo.mapper.QuestionMapper;
import com.yang.demo.model.Comment;
import com.yang.demo.model.Notice;
import com.yang.demo.model.User;
import com.yang.demo.provider.PageProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Yiang37
 * @Date 2020/3/25 14:05
 * Description:
 */
@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private PageProvider pageProvider;

    //查找用户的消息通知
    public MyNoticeAndPageDTO findMyNoticesAndPage(String page, String size, Integer id) {
        //需要先计算总条数
        String tatalCount = noticeMapper.findNoticeTotalCountByReceiverId(id);
        //矫正页码 为啥要写这个 -1可以直接设置为1 但是最后一页得计算
        //由于计算总页数要size 所以size得先矫正下
        String checkedSize = pageProvider.checkSizeNow(size);
        String checkedPage = pageProvider.checkPageNow(page, tatalCount, checkedSize);
        //page与size范围矫正好了 再进行使用

        //1.获取问题列表
        //计算开始数
        String startNum = pageProvider.startNum(checkedPage, checkedSize);
        List<MyNoticeDTO> myNoticeDTOList = new ArrayList<>();
        //得到问题列表并做好转换
        List<Notice> dbNoticeList = noticeMapper.findNoticeListByReceiverId(startNum, checkedSize, id);
        for (Notice notice : dbNoticeList) {
            MyNoticeDTO myNoticeDTO = new MyNoticeDTO();
            myNoticeDTO.setId(notice.getId());
            myNoticeDTO.setNtype(notice.getNtype());
            myNoticeDTO.setOuterId(notice.getOuterId());
            myNoticeDTO.setCreatorName(notice.getCreatorName());
            myNoticeDTO.setNstate(notice.getNstate());
            myNoticeDTO.setOuterTitle(notice.getOuterTitle());
            myNoticeDTO.setGmtCreate(notice.getGmtCreate());
            //点击的id 是问题就是问题 是评论就要找到对应的问题
            if (myNoticeDTO.getNtype() == CommentTypeEnums.QUESTION_COMMENT.getType()){
                myNoticeDTO.setClickId(notice.getOuterId());
            }
            if (myNoticeDTO.getNtype() == CommentTypeEnums.COMMENT_COMMENT.getType()){
                //查找评论对应的问题
                Comment dbComment = commentMapper.findCommentByCommentId(notice.getOuterId());
                myNoticeDTO.setClickId(dbComment.getParentId());
            }

            myNoticeDTOList.add(myNoticeDTO);
        }

        //2.获取页码数据 //也要矫正页码和大小
        PageDTO pageDTO = pageProvider.gainPageDTO(checkedPage, checkedSize, tatalCount, 3);
        //设置页面对象
        MyNoticeAndPageDTO myNoticeAndPageDTO = new MyNoticeAndPageDTO();
        myNoticeAndPageDTO.setMyNoticeDTOList(myNoticeDTOList);
        myNoticeAndPageDTO.setPageDTO(pageDTO);

        return myNoticeAndPageDTO;
    }

    //查找未读消息数目
    public Long findUnReadNumber(Integer id) {
        Long num = noticeMapper.findNstate1Count(id);
        if (null == num){
            return 0L;
        }
        return num;
    }

    //读取一个消息
    public void readOneNotification(Integer id, User user) {
        Notice dbNotice = noticeMapper.selectNoticeById(id);
        dbNotice.setNstate(NoticeStateEnums.READ.getState());
        //更新即为读取
        noticeMapper.updateNstateById(dbNotice.getId());
    }

}