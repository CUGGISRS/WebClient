package com.github.wxiaoqi.security.zs.sys.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.github.wxiaoqi.security.common.handler.FeignErrorDecoder;
import com.github.wxiaoqi.security.common.handler.GlobalExceptionHandler;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration("zsSysWebConfig")
@Primary
@Data
@RefreshScope
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    /**
     * 传感器数据调用同步接口的时间间隔
     */
    @Value("${Scheduled.DeviceSensorData.interval}")
    private long interval;

    /**
     * 传感器数据调用同步接口的分页查询物联网限制条数
     */
    @Value("${Scheduled.DeviceSensorData.limit}")
    private long limit;


    @Value("${aliyun.accessKeyID}")
    private String accessKeyID;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
    //指定地域名称 短信API的就是 cn-hangzhou 不能改变
    @Value("${aliyun.regionId}")
    private String regionId;

    //短信api的请求地址  固定
    @Value("${aliyun.domain}")
    private String domain;
    //您的申请签名
    @Value("${aliyun.signName}")
    private String signName;
    //传感器报警短信模板
    @Value("${aliyun.templateCode}")
    private String templateCode;

    //企业审核短信模板
    @Value("${aliyun.templateCode1}")
    private String templateCode1;

    //企业注册提示短信模板
    @Value("${aliyun.templateCode2}")
    private String templateCode2;

    //API的版本，固定值,如短信API的值为：2017-05-25
    @Value("${aliyun.version}")
    private String version;
    //API的命名，固定值,如发送短信API的值为：SendSms
    @Value("${aliyun.action}")
    private String action;

    /**
     * 短信配置
     */
    @Bean
    public IAcsClient SendSmsSender() {
        // 指定地域名称 短信API的就是 cn-hangzhou 不能改变  后边填写您的  accessKey 和 accessKey Secret
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyID, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    /**
     * feign统一异常
     */
    @Bean
    FeignErrorDecoder getFeignErrorDecoder() {
        return new FeignErrorDecoder();
    }

    /**
     * 网关、Controller统一异常处理
     */
    @Bean
    GlobalExceptionHandler getGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }


    /**
     * 配置资源映射路径
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }




/*

    @Bean
    GlobalExceptionHandler getGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        return factory.createMultipartConfig();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getServiceAuthRestInterceptor()).
                addPathPatterns(getIncludePathPatterns());
        registry.addInterceptor(getUserAuthRestInterceptor()).
                addPathPatterns(getIncludePathPatterns());
    }

    @Bean
    ServiceAuthRestInterceptor getServiceAuthRestInterceptor() {
        return new ServiceAuthRestInterceptor();
    }
    @Bean
    UserAuthRestInterceptor getUserAuthRestInterceptor() {
        return new UserAuthRestInterceptor();
    }


    */
/**
 * 需要服务认证判断的路径
 *
 * @return
 *//*

    private ArrayList<String> getIncludePathPatterns() {
        ArrayList<String> list = new ArrayList<>();
        //需要验证token的url
        String[] urls = {
                "/Sale/**"
        };
        Collections.addAll(list, urls);
        return list;
    }
*/


}
