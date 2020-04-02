package com.yang.demo.mapper;

import com.yang.demo.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    //插入一个新问题
    @Insert("insert into questions (user_id,title,description,tag,gmt_create,gmt_modified,view_count,like_count,comment_count) values (#{userId},#{title},#{description},#{tag},#{gmtCreate},#{gmtModified},#{viewCount},#{likeCount},#{commentCount})")
    Integer insertOneQuestion(Question tempQuestion);

    //更新一个问题
    @Update("update questions set title=#{title},description=#{description},tag=#{tag},gmt_modified=#{gmtModified} where id =#{id}")
    Integer updateOneQuestion(Question tempQuestion);

    //分页查询问题 返回问题列表
    @Select("select * from questions ORDER BY gmt_modified  DESC limit ${startNum},${size}")
    List<Question> findQuestionList(String startNum, String size);

    //获取问题的总条数
    @Select("SELECT COUNT(*) FROM questions")
    String findQuestionTotalCount();

    //根据问题id查询得到一个问题
    @Select("select * from questions where id = #{id}")
    Question findQuestionByQuestionId(String id);

    //根据问题id查找问题的发布者的Id
    @Select("select user_id from questions where id = #{id}")
    Integer findUserIdByQuestionId(String questionId);

    //根据问题id删除一个问题
    @Delete("delete from questions where id = #{id}")
    Integer deleteQuestionByQuestionId(String questionId);

    //更新问题的评论数 +1
    @Update("update questions set comment_count = comment_count+1 where id = #{id}")
    void addOneQuestionCommentCount(Integer questionId);

    //查询问题的评论数
    @Select("select comment_count from questions where id = #{id}")
    Integer findCommentCountByQuestionId(String qid);

    //分页查询某个用户发布的问题
    @Select("select * from questions WHERE user_id = #{id} ORDER BY gmt_modified  DESC limit ${startNum},${size}")
    List<Question> findQuestionListByUserId(@Param("startNum") String startNum,@Param("size") String checkedSize,@Param("id") Integer id);

    //查找某用户发布的问题总数
    @Select("SELECT count(1) FROM questions WHERE user_id = #{id}")
    String findQuestionTotalCountByUserId(Integer id);

    //查询相关问题列表
    @Select("select * from questions where id != #{id} and tag regexp #{tag}")
    List<Question> findAboutQuestions(@Param("id") Integer id, @Param("tag") String regTag);

    //搜索时查找到的问题总数
    @Select("select count(*) from questions where title regexp #{search}")
    String selectQuestionQueryCounts(String regsearch);

    //搜索时查找到的问题列表
    @Select("select * from questions where title regexp #{search} order by id desc limit ${offset},${size} ")
    List<Question> findQuestionQueryList(@Param("offset")String offset,@Param("size") String size, @Param("search") String search);

    @Update("update questions set view_count = view_count + 1 where id = #{questionId}")
    void increaseOneViewCount(String questionId);
}
