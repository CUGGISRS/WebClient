package com.github.wxiaoqi.security.dzsw.sys.config.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 阿里短信
 * @author: gsy
 * @create: 2020-09-26 14:13
 **/
@Configuration
public class SendSmsConfig {
    @Value("${aliyun.accessKeyID}")
    private String accessKeyID;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
    //指定地域名称 短信API的就是 cn-hangzhou 不能改变
    @Value("${aliyun.regionId}")
    private String regionId;

    @Bean
    public IAcsClient SendSmsSender() {
        // 指定地域名称 短信API的就是 cn-hangzhou 不能改变  后边填写您的  accessKey 和 accessKey Secret
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyID, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
}
