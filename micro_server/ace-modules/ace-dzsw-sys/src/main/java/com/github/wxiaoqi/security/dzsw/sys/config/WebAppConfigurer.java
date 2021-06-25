package com.github.wxiaoqi.security.dzsw.sys.config;

import com.github.wxiaoqi.security.common.handler.FeignErrorDecoder;
import com.github.wxiaoqi.security.common.handler.GlobalExceptionHandler;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


/**
 * 自定义资源映射
 *
 * @author TsaiJun
 */
@Configuration
@Data
public class WebAppConfigurer implements WebMvcConfigurer {

    @Autowired
    private UserTokenArgumentResolverUP userTokenArgumentResolver;

    //子目录
    @Value("${UploadFile.VIDEO_PATH}")
    private String VIDEO_PATH;

    //子目录
    @Value("${UploadFile.SON_PATH}")
    private String SON_PATH;

    // 缩略图
    @Value("${UploadFile.THUMB_PATH}")
    private String THUMB_PATH;

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
    com.github.wxiaoqi.security.common.handler.GlobalExceptionHandler getGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * 自定义资源映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler(VIDEO_PATH + "**").addResourceLocations("file:" + ROOT_PATH + "/UploadFile/dzswVideos/")
                .setCachePeriod(3600);//设置静态文件缓存  单位秒   ，不设则不缓存
        registry.addResourceHandler(THUMB_PATH + "**").addResourceLocations("file:" + ROOT_PATH + "/UploadFile/dzswThumbnail/")
                .setCachePeriod(3600);
        registry.addResourceHandler(SON_PATH + "**").addResourceLocations("file:" + ROOT_PATH + "/UploadFile/dzswImg/")
                .setCachePeriod(3600);
    }

    /**
     * 注册自定义参数解析器
     *
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userTokenArgumentResolver);
    }


    /**
     * 允许前端跨域
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }
}
