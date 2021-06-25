package com.github.wxiaoqi.security.dzsw.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.dzsw.sys.config.exception.SmsException;
import com.github.wxiaoqi.security.dzsw.sys.service.SendSmsService;
import com.github.wxiaoqi.security.dzsw.sys.utils.sms.AssertUtil;
import com.github.wxiaoqi.security.dzsw.sys.utils.sms.RedisUtil;
import com.github.wxiaoqi.security.dzsw.sys.utils.sms.SmsUtil;
import com.github.wxiaoqi.security.dzsw.sys.vo.SendSmsDto;
import com.github.wxiaoqi.security.dzsw.sys.vo.VerifyCodeDto;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 短信服务
 * @author: gsy
 * @create: 2020-09-26 13:24
 **/
@Service
@Data
public class SendSmsServiceImpl implements SendSmsService {

    //    private static final Logger logger = LoggerFactory.getLogger(SendSmsServiceImpl.class);
    //aliyuncs的参数
    //指定地域名称 短信API的就是 cn-hangzhou 不能改变
    @Value("${aliyun.regionId}")
    private String regionId;

    //短信api的请求地址  固定
    @Value("${aliyun.domain}")
    private String domain;
    //您的申请签名
    @Value("${aliyun.signName}")
    private String signName;
    //你的模板
    @Value("${aliyun.templateCode}")
    private String templateCode;
    //验证码失效时间
    @Value("${aliyun.invalidtime}")
    private String INVALID_TIME;
    @Resource
    private IAcsClient iAcsClient;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 发送短信接口
     *
     * @param sendSmsDto
     * @return
     */
    @Override
    public boolean sendSms(SendSmsDto sendSmsDto) {
        //生成随机数
        String code = SmsUtil.getCode();
        // 创建通用的请求对象
        CommonRequest request = new CommonRequest();
        // 指定请求方式
        request.setSysMethod(MethodType.POST);
        // 短信api的请求地址  固定
        request.setSysDomain(domain);
        //签名算法版本  固定
        request.setSysVersion("2017-05-25");
        //请求 API 的名称
        request.setSysAction("SendSms");
        //指定地域名称
        request.putQueryParameter("RegionId", regionId);
        // 要给哪个手机号发送短信  指定手机号
        request.putQueryParameter("PhoneNumbers", sendSmsDto.getMobile());
        // 您的申请签名
        request.putQueryParameter("SignName", signName);
        // 您申请的模板 code
        request.putQueryParameter("TemplateCode", templateCode);

        Map<String, Object> params = new HashMap<>();
        //这里的key就是短信模板中的 ${xxxx}
        params.put("code", code);

        // 放入参数  需要把 map转换为json格式  使用fastJson进行转换
        request.putQueryParameter("TemplateParam", JSON.toJSONString(params));

        try {
            CommonResponse response = iAcsClient.getCommonResponse(request);
//            logger.info(JSON.parseObject(response.getData(), Map.class).get("Message").toString());
            //存入redis 五分钟:60 * 5
            if (!redisUtil.setCacheObject(SmsUtil.map.get(sendSmsDto.getType()) + sendSmsDto.getMobile(),
                    code, 60 * Integer.parseInt(INVALID_TIME))) {
                throw new SmsException(StatusCode.SMS_SEND_ERROR, "");
            }
            return response.getHttpResponse().isSuccess();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void verifyCode(VerifyCodeDto verifyCodeDto) {
        String s = SmsUtil.map.get(verifyCodeDto.getType());
        String mobile = verifyCodeDto.getMobile();

        String redisCode = (String) redisUtil.getCacheObject(SmsUtil.map
                .get(verifyCodeDto.getType()) + verifyCodeDto.getMobile());
        AssertUtil.isNull(redisCode, StatusCode.FAIL);
        if (redisCode.equals(verifyCodeDto.getCode())) {
            redisUtil.delete(SmsUtil.map.get(verifyCodeDto.getType()) + verifyCodeDto.getMobile());
        } else {
            throw new SmsException(StatusCode.SMS_ERROR, "");
        }
    }

}

