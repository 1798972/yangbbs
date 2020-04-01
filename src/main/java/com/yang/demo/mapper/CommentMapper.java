package com.yang.demo.mapper;

import com.yang.demo.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {


    //插入一条评论记录
    @Insert("insert into comments(ctype,parent_id,user_id,content,like_count,comment_count,gmt_create,gmt_modified) values (#{ctype},#{parentId},#{userId},#{content},#{likeCount},#{commentCount},#{gmtCreate},#{gmtModified})")
    int inserOneComment(Comment dbComment);

    //根据父id和类型找到评论列表
    @Select("SELECT * FROM comments WHERE parent_id = #{qid} AND ctype = #{type} ORDER BY gmt_modified DESC limit ${rows};")
    List<Comment> findCommentByParentId(@Param("qid") String qid, @Param("type") int type, @Param("rows") Integer rows);

    //评论的评论数+1
    @Update("update comments set comment_count = comment_count+1 where id = #{id}")
    void addOneCommentCommnetCount(Integer parentId);

    //根据评论id找到评论总数
    @Select("select comment_count from comments where id = #{cid}")
    Integer findCommentCountByCommentId(String cid);

    //根据id找到评论
    @Select("select * from comments where id = #{id}")
    Comment findCommentByCommentId(Integer id);
}
