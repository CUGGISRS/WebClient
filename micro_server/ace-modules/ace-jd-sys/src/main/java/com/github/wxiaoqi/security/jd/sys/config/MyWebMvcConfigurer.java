package com.github.wxiaoqi.security.jd.sys.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wxiaoqi.security.common.handler.DateConverter;
import com.github.wxiaoqi.security.common.handler.FeignErrorDecoder;
import com.github.wxiaoqi.security.common.handler.GlobalExceptionHandler;
import com.github.wxiaoqi.security.common.handler.JsonDateConverter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;

@Configuration
@Data
public class MyWebMvcConfigurer implements WebMvcConfigurer {
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


}
