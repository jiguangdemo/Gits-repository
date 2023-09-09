package com.sdau.nemt.service.user.util;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import java.util.*;
import com.aliyuncs.dysmsapi.model.v20170525.*;
/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
public class PhoneUtils {
    public static void sendPhone(String phone,String code) {

        DefaultProfile profile = DefaultProfile.getProfile("cn-qingdao", "LTAI5tQptSpZW4Ti95218HL3", "1MZnY8bGwAck7Pg3VgrIbxknazMjfK");
        /** use STS Token
         DefaultProfile profile = DefaultProfile.getProfile(
         "<your-region-id>",           // The region ID
         "<your-access-key-id>",       // The AccessKey ID of the RAM account
         "<your-access-key-secret>",   // The AccessKey Secret of the RAM account
         "<your-sts-token>");          // STS Token
         **/

        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName("xwh图书管理系统");
        request.setTemplateCode("SMS_246075268");
        HashMap<String,Object> map = new HashMap<>();
        map.put("code",code);
        request.setTemplateParam(JSONObject.toJSONString(map));

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

    }
}
