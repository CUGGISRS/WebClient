package com.emacsist.upload.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;


/**
 * 自定义资源映射
 *
 * @author TsaiJun
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
    @Autowired
    private FileUploadConfig fileUploadConfig;
    @Autowired
    private UserTokenArgumentResolverUP userTokenArgumentResolver;

    /**
     * 自定义资源映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/play/**").addResourceLocations("file:" + fileUploadConfig.getVideoSavePath());
        registry.addResourceHandler("/show/**").addResourceLocations("file:" + fileUploadConfig.getThumbnailSavePath());
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
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


//    /**
//     * 允许前端跨域
//     * @param registry
//     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
//                .maxAge(3600)
//                .allowCredentials(true);
//    }
}
