package com.github.wxiaoqi.security.gate.config;

import com.github.wxiaoqi.security.gate.handler.RequestBodyRoutePredicateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ace
 * @create 2019/1/27.
 */
@Configuration
public class GatewayConfig {


/*
    @Bean
    RequestBodyRoutePredicateFactory requestBodyRoutePredicateFactory() {
        return new RequestBodyRoutePredicateFactory();
    }
*/

    /**
     *解决表单乱码（无效？）
     * @return
     */
  /*  @Bean
    FormHttpMessageConverter formHttpMessageConverter(){
        FormHttpMessageConverter converter=new FormHttpMessageConverter();
        List<HttpMessageConverter<?>> partConverters = new ArrayList();

        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);

        partConverters.add(new ByteArrayHttpMessageConverter());
        partConverters.add(stringHttpMessageConverter);
        partConverters.add(new ResourceHttpMessageConverter());
        converter.setPartConverters(partConverters);
        return  converter;
    }*/

}

