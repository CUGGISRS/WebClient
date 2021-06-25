package com.github.wxiaoqi.security.zs.sys.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.github.wxiaoqi.security.common.msg.StatusCode;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信工具类
 */
public class SendSmsUtil {

    /**
     * 发送短信接口
     */
    public static boolean sendSms(String phone, String signName, String templateCode, String action, String version,
                                  String regionId, String domain, IAcsClient iAcsClient, Map<String, Object> params) {

        // 创建通用的请求对象
        CommonRequest request = new CommonRequest();
        // 指定请求方式
        request.setSysMethod(MethodType.POST);
        // 短信api的请求地址  固定
        request.setSysDomain(domain);
        //签名算法版本  固定
        request.setSysVersion(version);
        //请求 API 的名称
        request.setSysAction(action);
        //指定地域名称
        request.putQueryParameter("RegionId", regionId);
        // 要给哪个手机号发送短信  指定手机号
        request.putQueryParameter("PhoneNumbers", phone);
        // 您的申请签名
        request.putQueryParameter("SignName", signName);
        // 您申请的模板 code
        request.putQueryParameter("TemplateCode", templateCode);

        //这里的key就是短信模板中的 ${xxxx}  放入参数  需要把 map转换为json格式  使用fastJson进行转换
        request.putQueryParameter("TemplateParam", JSON.toJSONString(params));

        try {
            CommonResponse response = iAcsClient.getCommonResponse(request);
            String data = response.getData();
            if (data != null) {
                JSONObject dataJson = JSONObject.parseObject(data);
                String code = dataJson.getString("Code");
                if ("OK".equals(code)) {
                    return true;
                }
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
