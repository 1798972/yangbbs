package com.yang.demo.service;

import com.yang.demo.dto.MyInfomationDTO;
import com.yang.demo.dto.RegisterUserDTO;
import com.yang.demo.enums.UserTypeEnums;
import com.yang.demo.mapper.UserAuthMapper;
import com.yang.demo.mapper.UserMapper;
import com.yang.demo.model.User;
import com.yang.demo.model.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.UUID;

/**
 * @Author Yiang37
 * @Date 2020/3/1 0:32
 * Description:
 */
@Service
public class UserService {

    @Autowired
    UserAuthMapper userAuthMapper;
    @Autowired
    UserMapper userMapper;

    //1.根据传入认证表和用户信息 判断新增还是更新用户信息
    //登录后只能拿到opendId以及用户类型 userId是不一定有的
    @Transactional
    public void insertOrUpdateUserInfo(User tempUser, UserAuth tempUserAuth) {
        //根据 类型/认证码 查找是否存在该记录
        UserAuth userAuth = userAuthMapper.findAuthByTypeAndVerification(tempUserAuth.getUType(), tempUserAuth.getVerification());

        if (userAuth != null) {
            //有记录是老用户 认证信息存在了 且认证信息此时无需修改
            //注意根据认证码查找不一定唯一
            Integer userId = userAuthMapper.findUserIdByAuthId(userAuth.getId());
            User dbUser = userMapper.findUserById(userId);
            //查出对应的用户信息 更新老用户信息
            dbUser.setNickname(tempUser.getNickname());
            dbUser.setAvatar(tempUser.getAvatar());
            dbUser.setToken(tempUser.getToken());
            dbUser.setGmtModified(System.currentTimeMillis());
            //更新老用户信息
            userMapper.updateOneUserInfo(dbUser);
        } else {
            //新用户 补全数据库信息
            //之后 创建新用户并创建新认证信息
            tempUser.setGmtCreate(System.currentTimeMillis());
            tempUser.setGmtModified(System.currentTimeMillis());
            //创建新用户
            userMapper.insertOneUser(tempUser);
            Integer nowInsertId = userMapper.findNowInserId();
            //根据创建的新用户的id号 建立新的认证信息记录
            UserAuth newAuth = new UserAuth();
            newAuth.setUserId(nowInsertId);
            newAuth.setUType(tempUserAuth.getUType());
            newAuth.setVerification(tempUserAuth.getVerification());
            userAuthMapper.insertOneAuth(newAuth);
        }
    }

    //2.注册时检查用户名
    public Integer checkRegisterName(String regisName) {
        User user = userMapper.findUserByLoginName(regisName);
        if (user == null) {
            return 0;
        }
        return -1;
    }

    //3.注册一个新的用户
    @Transactional
    public boolean registerLocalUser(RegisterUserDTO registerUserDTO) {
        //先新建一个用户
        User tempUser = new User();
        String token = UUID.randomUUID().toString();
        //这个头像是必填的字段 那我用我的
        String adminAvatarUrl = userMapper.findAdminAvatar(1);
        tempUser.setLoginName(registerUserDTO.getUsername());
        tempUser.setNickname("一个没有昵称的人");
        tempUser.setAvatar(adminAvatarUrl);
        tempUser.setToken(token);
        tempUser.setGmtCreate(System.currentTimeMillis());
        tempUser.setGmtModified(System.currentTimeMillis());
        userMapper.insertOneUser(tempUser);
        //再新建2条认证记录 0-密码 1-手机号
        UserAuth tempAuth = new UserAuth();
        Integer nowInsertId = userMapper.findNowInserId();
        tempAuth.setUserId(nowInsertId);
        tempAuth.setUType(UserTypeEnums.WebUser.getUserType());
        tempAuth.setVerification(registerUserDTO.getPassword());
        userAuthMapper.insertOneAuth(tempAuth);
        UserAuth tempAuth2 = new UserAuth();
        tempAuth2.setUserId(nowInsertId);
        tempAuth2.setUType(UserTypeEnums.TelUser.getUserType());
        tempAuth2.setVerification(registerUserDTO.getTel());
        userAuthMapper.insertOneAuth(tempAuth2);
        return true;
    }

    //检测手机是否被注册过 true注册过/false没有
    public boolean checkTelHasRegistered(String tel) {
        UserAuth userAuth = userAuthMapper.findAuthByTypeAndVerification(3, tel);
        return userAuth == null;
        //若为true 说明为空 没注册过
        //若为false 说明注册过了
    }

    //检查用户名和密码是否匹配
    public boolean loginCheck(String loginName, String loginWord, String token) {
        User dbUser = userMapper.findUserByLoginName(loginName);
        if (dbUser == null) {
            return false;
        }
        UserAuth dbUserAuth = userAuthMapper.findAuthByUserIdAndType(dbUser.getId(), UserTypeEnums.WebUser.getUserType());
        if (loginWord.equals(dbUserAuth.getVerification())) {
            //登录成功 更新token
            dbUser.setToken(token);
            userMapper.updateOneUserInfo(dbUser);
            return true;
        }
        return false;
    }

    //根据token查找用户
    public User findUserByToken(String token) {
        User dbUser = userMapper.findUserByToken(token);
        return dbUser;
    }

    //根据用户id找到个人信息
    public MyInfomationDTO findUserInfomationByUerId(Integer id) {
        MyInfomationDTO myInfomationDTO = new MyInfomationDTO();
        User dbUser = userMapper.findUserById(id);
        myInfomationDTO.setId(dbUser.getId());
        myInfomationDTO.setAvatar(dbUser.getAvatar());
        myInfomationDTO.setNickname(dbUser.getNickname());
        //羊羊号
        if (null == dbUser.getLoginName()) {
            myInfomationDTO.setYyNumber("未注册");
        } else {
            myInfomationDTO.setYyNumber(dbUser.getLoginName());
        }
        UserAuth dbUserAuth = null;
        //电话号
        dbUserAuth = userAuthMapper.findAuthByUserIdAndType(id, UserTypeEnums.TelUser.getUserType());
        if (null == dbUserAuth) {
            myInfomationDTO.setTelNumber("未注册");
        } else {
            myInfomationDTO.setTelNumber(dbUserAuth.getVerification());
        }
        //QQ号
        dbUserAuth = userAuthMapper.findAuthByUserIdAndType(id, UserTypeEnums.QQUser.getUserType());
        if (null == dbUserAuth) {
            myInfomationDTO.setQqNumber("未绑定");
        } else {
            myInfomationDTO.setQqNumber("已绑定");
        }
        //GitHub账号
        dbUserAuth = userAuthMapper.findAuthByUserIdAndType(id, UserTypeEnums.GitHubUser.getUserType());
        if (null == dbUserAuth) {
            myInfomationDTO.setGithubNumber("未绑定");
        } else {
            myInfomationDTO.setGithubNumber("已绑定");
        }

        myInfomationDTO.setGmtCreate(dbUser.getGmtCreate());
        return myInfomationDTO;
    }

    //根据用户id查找用户类型
    public int findUserTypeById(String userId) {
        List<Integer> typeList = userMapper.findUserTypeById(userId);

        //目前只有网站原生用户需要更改资料 所以有的话返回0 没有返回-1
        for (Integer type : typeList) {
            if (type == 0){
                return 0;
            }
        }
        return -1;

    }

    //更新一个用户的信息
    public int changeOneUserInfo(String id, String newNickname, String newAvaUrl) {
        int row = userMapper.updateOneUserNicknameAndAvator(id,newNickname,newAvaUrl);
        return row;
    }
}