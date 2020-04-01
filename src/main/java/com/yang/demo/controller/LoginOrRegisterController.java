package com.yang.demo.controller;

import com.yang.demo.dto.RegisterStateDTO;
import com.yang.demo.dto.RegisterUserDTO;
import com.yang.demo.dto.SendSmsDTO;
import com.yang.demo.enums.RegisterStateEnums;
import com.yang.demo.enums.TelStateEnums;
import com.yang.demo.provider.ImageCoedProvider;
import com.yang.demo.provider.SmsProvider;
import com.yang.demo.service.SendSmsService;
import com.yang.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Yiang37
 * @Date 2020/3/1 14:21
 * Description:
 */

@Controller
public class LoginOrRegisterController {

    @Autowired
    UserService userService;
    @Autowired
    SmsProvider smsProvider;
    @Autowired
    SendSmsService sendSmsService;

    private static String ImgCode4;

    //0.用户登录
    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam("loginUsername")String loginName,
                        @RequestParam("loginPassword")String loginWord,
                        HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        boolean loginFlag = userService.loginCheck(loginName,loginWord,token);
        if (loginFlag){
            response.addCookie(new Cookie("yangtoken",token));
            return "success";
        }else {
        return "error";
        }
    }

    //0.点击普通登录
    @GetMapping("/login")
    public String clickLogin() {
        return "loginorregister";
    }

    //0.用户注册 检测用户名和手机号是否可用
    @RequestMapping("/register")
    @ResponseBody
    public RegisterStateDTO register(@ModelAttribute RegisterUserDTO registerUserDTO){
        //使用@ModelAttribute这个方式时 RegisterUser类的属性名必须与请求中的键名对应！！前后端要一致！！
        //error success
        RegisterStateDTO registerStateDTO = new RegisterStateDTO();
        //先判断是否有空
        if (registerUserDTO.getUsername() == null || registerUserDTO.getPassword()==null || registerUserDTO.getTel()==null){
            registerStateDTO.setState("error");
            registerStateDTO.setCode(RegisterStateEnums.USERINFO_HAS_NULL.getCode());
            registerStateDTO.setDes(RegisterStateEnums.USERINFO_HAS_NULL.getDes());
            return registerStateDTO;
        }
        //验证合法性 用户名+密码+手机号
        //1.用户名 4到16位(字母、数字、下划线、减号)
//        /^[a-zA-Z0-9_-]{4,16}$/;
        String regexUserName = "^[a-zA-Z0-9_-]{4,16}$";
        Pattern p = Pattern.compile(regexUserName);
        Matcher m = p.matcher(registerUserDTO.getUsername());
        if (!m.matches()){
            registerStateDTO.setState("error");
            registerStateDTO.setCode(RegisterStateEnums.USERNAMW_FORMAT_ERROR.getCode());
            registerStateDTO.setDes(RegisterStateEnums.USERNAMW_FORMAT_ERROR.getDes());
            return registerStateDTO;
        }
        //还要检测用户名是否被注册过！哎呀之前怎么写个0作为可注册
        // 就因为查找数量为0表示可以注册? 可是这样要绕个弯 下次避免这种写法
        Integer status = userService.checkRegisterName(registerUserDTO.getUsername());
        if(status != 0){
            //不可注册!
            registerStateDTO.setState("error");
            registerStateDTO.setCode(RegisterStateEnums.USERNAME_HAS_REGISTER.getCode());
            registerStateDTO.setDes(RegisterStateEnums.USERNAME_HAS_REGISTER.getDes());
            return registerStateDTO;
        }
        //2.密码 6到16位(需同时包含字母、数字、特殊符号)
        String regexPassword = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/]).{6,16}$";
        p = Pattern.compile(regexPassword);
        m = p.matcher(registerUserDTO.getPassword());
        if (!m.matches()){
            registerStateDTO.setState("error");
            registerStateDTO.setCode(RegisterStateEnums.PASSWOED_FORMAT_ERROR.getCode());
            registerStateDTO.setDes(RegisterStateEnums.PASSWOED_FORMAT_ERROR.getDes());
            return registerStateDTO;
        }
        //3.手机号
        String regexTel = "^1[34578]\\d{9}$";
        p = Pattern.compile(regexTel);
        m = p.matcher(registerUserDTO.getTel());
        if (!m.matches()){
            registerStateDTO.setState("error");
            registerStateDTO.setCode(RegisterStateEnums.TEL_FORMAT_ERROR.getCode());
            registerStateDTO.setDes(RegisterStateEnums.TEL_FORMAT_ERROR.getDes());
            return registerStateDTO;
        }

        //手机被注册过
        if (!userService.checkTelHasRegistered(registerUserDTO.getTel())){
                //被注册过
                registerStateDTO.setState("error");
                registerStateDTO.setCode(TelStateEnums.TEL_USED.getErrorCode());
                registerStateDTO.setDes(TelStateEnums.TEL_USED.getDes());
                return registerStateDTO;
        }
        //校验完成 开始注册
        boolean resFlag = userService.registerLocalUser(registerUserDTO);
       if (resFlag){
           //注册成功
           registerStateDTO.setState("success");
           registerStateDTO.setCode(RegisterStateEnums.REGISTER_SUCCESS.getCode());
           registerStateDTO.setDes(RegisterStateEnums.REGISTER_SUCCESS.getDes());
           return registerStateDTO;
       }else {
           registerStateDTO.setState("error");
           registerStateDTO.setCode(RegisterStateEnums.REGISTER_ERROR.getCode());
           registerStateDTO.setDes(RegisterStateEnums.REGISTER_ERROR.getDes());
           return registerStateDTO;
       }
    }

    //1.检查用户名是否可用
    @GetMapping("/register/checkname")
    @ResponseBody
    public Integer checkRegisterName(@RequestParam("regisName") String regisName) {
        //数据库查找用户名
        Integer status = userService.checkRegisterName(regisName);
        if (status == 0) {
            //未找到用户名 可以注册
            return 0;
        } else {
            //用户名已经存在
            return -1;
        }
    }

    //2.注册时发送短信验证码
    @PostMapping("/register/send6code")
    @ResponseBody
    public SendSmsDTO sendRegisterTelCode(@RequestParam("telNum") String telNumber) {
        return sendSmsService.checkTelUsedAndSend6Code(telNumber);
    }

    //3.生成图片验证码
    @RequestMapping("/register/getImgCode")
    public void getImgCode(HttpServletResponse response, HttpServletRequest request){
        try {
            String code =  ImageCoedProvider.createcode();
            ImgCode4 = code;
            BufferedImage image = ImageCoedProvider.createimage();

            //以流的方式返回给客户端
            response.setContentType("image/jpeg");
            ByteArrayOutputStream bt = new ByteArrayOutputStream();
            //将图片转换成字节流
            ImageIO.write(image,"jpeg",bt);
            //得到输出流，返回客户端
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bt.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //4.回传图片验证码的值
    // 唉用cookie和session老出错
    // 暂时这样凑合着用吧
    @ResponseBody
    @PostMapping("/register/get4Code")
    public String get4Code(){
        return LoginOrRegisterController.ImgCode4;
    }

    //5.检查手机号是否被使用过
    @PostMapping("/register/chickTel")
    @ResponseBody
    public String telHasUsed(@RequestParam("telNumber") String telNum){
        //返回字符串时 不加这个body就会返回模板页 记得加上
        boolean flag = userService.checkTelHasRegistered(telNum);
        if (flag){
            return "canUse";
        }else {
            return "hasUsed";
        }
    }
}
