package com.github.wxiaoqi.security.info.sys.config.system;

import com.github.wxiaoqi.security.common.handler.FeignErrorDecoder;
import com.github.wxiaoqi.security.common.handler.GlobalExceptionHandler;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: 配置资源映射路径
 * @author: gsy
 * @create: 2020-09-10 14:51
 **/
@Configuration
@Data
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    //子目录
    @Value("${UploadFile.SON_PATH}")
    private String SON_PATH;

    //根目录
    @Value("${UploadFile.ROOT_PATH}")
    private String ROOT_PATH;

    //服务器ip
    @Value("${server.ip}")
    private String ip;

    //服务器端口
    @Value("${server.port}")
    private String port;

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


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/infoSys/**").addResourceLocations("file:F:/infoSys/");
        registry.addResourceHandler(SON_PATH + "**").addResourceLocations("file:" + ROOT_PATH + "/UploadFile/infoImg/")
                .setCachePeriod(3600);//设置静态文件缓存  单位秒   ，不设则不缓存
    }
}
