package com.yang.demo.mapper;

import com.yang.demo.model.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeMapper {
    //插入一条评论
    @Insert("insert into notices(ntype,creator_id,creator_name,outer_id,outer_title,receiver_id,nstate,gmt_create) values(#{ntype},#{creatorId},#{creatorName},#{outerId},#{outerTitle},#{receiverId},#{nstate},#{gmtCreate})")
    void insertOneNotice(Notice notice);

    //根据接收者id找到他所有的通知信息
    @Select("SELECT count(1) FROM notices WHERE receiver_id = #{id}")
    String findNoticeTotalCountByReceiverId(Integer id);

    //根据接收者id找到所有的评论
    @Select("select * from notices WHERE receiver_id = #{id} ORDER BY gmt_create DESC limit ${startNum},${size}")
    List<Notice> findNoticeListByReceiverId(@Param("startNum") String startNum, @Param("size") String checkedSize, @Param("id") Integer id);

    //根据用户id查找所有未读消息 即nstate为1
    @Select("SELECT COUNT(1) FROM notices WHERE  nstate = 1 AND receiver_id = #{id};")
    Long findNstate1Count(Integer id);

    //根据id查找一条通知记录
    @Select("select * from notices where id = #{id}")
    Notice selectNoticeById(Integer id);

    //更新读取状态
    @Update("update notices set nstate = 0 where id = #{id}")
    void updateNstateById(Integer id);
}
