package com.github.wxiaoqi.security.com.sys.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wxiaoqi.security.common.handler.DateConverter;
import com.github.wxiaoqi.security.common.handler.FeignErrorDecoder;
import com.github.wxiaoqi.security.common.handler.GlobalExceptionHandler;
import com.github.wxiaoqi.security.common.handler.JsonDateConverter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;

@Configuration
@Data
@RefreshScope
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    /**
     * 存放路径
     */
    @Value("${file.savePath}")
    private String savePath;

    /**
     * 文件访问路径前缀
     */
    @Value("${file.visitPrefix}")
    private String visitPrefix;

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
        registry.addResourceHandler(visitPrefix + "**").addResourceLocations("file:" + savePath)
                .setCachePeriod(3600);//设置静态文件缓存  单位秒   ，不设则不缓存
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }


}
