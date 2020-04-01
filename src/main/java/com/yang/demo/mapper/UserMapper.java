package com.yang.demo.mapper;

import com.yang.demo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    //1.根据 用户id 查找用户
    @Select("select * from users where id = #{id}")
    User findUserById(Integer userId);

    //2.插入一个新用户
    @Insert("insert into users (nickname,avatar,token,gmt_create,gmt_modified,login_name) values (#{nickname},#{avatar},#{token},#{gmtCreate},#{gmtModified},#{loginName})")
    void insertOneUser(User user);

    //3.查询刚才插入记录的id号
    @Select("select @@IDENTITY")
    Integer findNowInserId();

    //4.更新一个用户信息
    @Update("update users set nickname = #{nickname},avatar = #{avatar},token = #{token},gmt_modified = #{gmtModified} where id=#{id}")
    void updateOneUserInfo(User dbUser);

    //5.根据 用户名 查找一个用户
    @Select("select * from users where login_name = #{regisName}")
    User findUserByLoginName(String regisName);

    //6.查找管理员的头像地址
    @Select("select avatar from users where id = #{i}")
    String findAdminAvatar(int i);

    //7.根据 token 查找用户
    @Select("select * from users where token = #{token}")
    User findUserByToken(String token);

    //8.根据用户id 查找用户名字
    @Select("select nickname from users where id = #{userId}")
    String findUserNameByUserId(Integer userId);
}
