package com.github.wxiaoqi.security.news.config;


//import com.github.wxiaoqi.security.auth.client.interceptor.NewsAuthRestInterceptor;

import com.github.wxiaoqi.security.auth.client.interceptor.ServiceAuthRestInterceptor;
import com.github.wxiaoqi.security.common.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;

@Configuration("newsWebConfig")
@Primary
public class WebConfiguration implements WebMvcConfigurer {
    @Bean
    GlobalExceptionHandler getGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getServiceAuthRestInterceptor()).
               addPathPatterns(getIncludePathPatterns()).addPathPatterns("/api/news/validate");
        /*registry.addInterceptor(getNewsAuthRestInterceptor()).
                addPathPatterns(getIncludePathPatterns());*/
    }

   @Bean
  ServiceAuthRestInterceptor getServiceAuthRestInterceptor() {
       return new ServiceAuthRestInterceptor();
   }

  /*  @Bean
        NewsAuthRestInterceptor getNewsAuthRestInterceptor() {
        return new NewsAuthRestInterceptor();
    }*/

    /**
     * 需要新闻和服务认证判断的路径
     * @return
     */
    private ArrayList<String> getIncludePathPatterns() {
        ArrayList<String> list = new ArrayList<>();
        String[] urls = {
                "/news/news/**"
        };
        Collections.addAll(list, urls);
        return list;
    }







}
