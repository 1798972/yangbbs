package com.yang.demo.mapper;

import com.yang.demo.model.UserAuth;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserAuthMapper {

    //1.根据 识别码、用户类别 查找验证表记录
    @Select("select * from user_auths where verification = #{verification} and utype = #{uType}")
    UserAuth findAuthByTypeAndVerification(@Param("uType") Integer uType, @Param("verification") String verification);

    //2.根据 认证记录的id 查找UserId
    @Select("select user_id from user_auths where id = #{id}")
    Integer findUserIdByAuthId(Integer recordId);

    //3.插入一条新认证表记录
    @Insert("insert into user_auths (user_id,utype,verification) values (#{userId},#{uType},#{verification})")
    void insertOneAuth(UserAuth newAuth);

    //4.根据 用户id、用户类别 查找验证表记录
    @Select("select * from user_auths where user_id = #{id} and utype = #{userType}")
    UserAuth findAuthByUserIdAndType(@Param("id") Integer id, @Param("userType") int userType);
}
