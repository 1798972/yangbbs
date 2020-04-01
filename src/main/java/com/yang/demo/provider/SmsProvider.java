package com.yang.demo.provider;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @Author Yiang37
 * @Date 2020/3/2 0:26
 * Description:
 * 调用短信接口 完成短信发送操作
 */

//成功返回0 错误返回错误码
@Component
public class SmsProvider {

    @Value("${sms.sdk.id}")
    private int sdkId;
    @Value("${sms.app.key}")
    private String appKey;

    //返回值 错误码
    public Integer sendMessage(String phone, String code) {
        int resultCode = 1;
        try {
            String[] params = {code};
            SmsSingleSender ssender = new SmsSingleSender(sdkId, appKey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phone,
                    544614, params, "yang37网", "", "");
            resultCode = result.result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultCode;
    }
}