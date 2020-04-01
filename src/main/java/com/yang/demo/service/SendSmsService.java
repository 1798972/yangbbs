package com.yang.demo.service;

import com.yang.demo.dto.SendSmsDTO;
import com.yang.demo.enums.SuccessOrErrorStateEnums;
import com.yang.demo.enums.TelStateEnums;
import com.yang.demo.provider.SmsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Yiang37
 * @Date 2020/3/2 23:57
 * Description:
 * 调用腾讯云接口 发送短信
 */
@Service
public class SendSmsService {

    @Autowired
    SmsProvider smsProvider;
    @Autowired
    UserService userService;

    //判断是否被注册 再发送短信
    public SendSmsDTO checkTelUsedAndSend6Code(String telNumber){
        SendSmsDTO sendSmsDTO = new SendSmsDTO();
        boolean flag = userService.checkTelHasRegistered(telNumber);
        if (!flag){
            //手机号被注册过
            sendSmsDTO.setStatus(SuccessOrErrorStateEnums.ERROR.getState());
            sendSmsDTO.setBackCode(TelStateEnums.TEL_USED.getErrorCode());
            sendSmsDTO.setDes(TelStateEnums.TEL_USED.getDes());
            return sendSmsDTO;
        }
        return send6Code(telNumber);
    }
    //检查手机格式 发送短信
    public SendSmsDTO send6Code(String telNumber) {
        String regexTel = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        SendSmsDTO sendSmsDTO = new SendSmsDTO();

        //先检查手机号格式正确与否
        if (telNumber.length() != 11) {
            //长度错误
            sendSmsDTO.setStatus(SuccessOrErrorStateEnums.ERROR.getState());
            sendSmsDTO.setBackCode(TelStateEnums.LENGTH_ERROR.getErrorCode());
            sendSmsDTO.setDes(TelStateEnums.LENGTH_ERROR.getDes());
            return sendSmsDTO;
        }
        //正则验证手机号
        Pattern p = Pattern.compile(regexTel);
        Matcher m = p.matcher(telNumber);

        if (m.matches()) {
            //格式正确 可以发送验证码
            //获取6位数字验证码
            String verCode = String.valueOf((int) (Math.random() * 900000 + 100000));
            //发送短信 手机号 验证码
            int result = smsProvider.sendMessage(telNumber, verCode);
            //发送短信的状态
            if (result == 0) {
                //成功 返回 success + 验证码
                sendSmsDTO.setStatus(SuccessOrErrorStateEnums.SUCCESS.getState());
                sendSmsDTO.setBackCode(Integer.parseInt(verCode));
                sendSmsDTO.setDes("成功发送验证码");
                return sendSmsDTO;
            } else {
                //错误 返回 error + 错误码
                sendSmsDTO.setStatus(SuccessOrErrorStateEnums.ERROR.getState());
                sendSmsDTO.setBackCode(result);
                sendSmsDTO.setDes(TelStateEnums.SEND_SMS_ERROR.getDes());
                return sendSmsDTO;
            }
        } else {
            sendSmsDTO.setStatus(SuccessOrErrorStateEnums.ERROR.getState());
            sendSmsDTO.setBackCode(TelStateEnums.FORMAT_ERROR.getErrorCode());
            sendSmsDTO.setDes(TelStateEnums.FORMAT_ERROR.getDes());
            return sendSmsDTO;
        }
    }
}