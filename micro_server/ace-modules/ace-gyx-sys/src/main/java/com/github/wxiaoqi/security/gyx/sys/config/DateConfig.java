package com.github.wxiaoqi.security.gyx.sys.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wxiaoqi.security.common.handler.DateConverter;
import com.github.wxiaoqi.security.common.handler.JsonDateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;

/**
 * 全局时间转换器配置（在springCloud中放到MyWebMvcConfigurer中部分自动配置会失效）
 */
@Configuration
public class DateConfig {

    /**
     * 注入自定义json时间参数转换器
     */
    @Bean
    public JsonDateConverter getJsonDateConverter() {
        return new JsonDateConverter();
    }


    /**
     * 替换json时间参数转换器
     */
    @Bean
    public Jackson2ObjectMapperFactoryBean jackson2ObjectMapperFactoryBean(
            @Autowired
                    JsonDateConverter dateJacksonConverter) {
        Jackson2ObjectMapperFactoryBean jackson2ObjectMapperFactoryBean = new Jackson2ObjectMapperFactoryBean();

        jackson2ObjectMapperFactoryBean.setDeserializers(dateJacksonConverter);
        return jackson2ObjectMapperFactoryBean;
    }

    /**
     * 注入全局统一日期转换器
     */
    @Bean
    public DateConverter getDateConverter() {
        return new DateConverter();
    }


    /**
     * 转换json时，使时间不转换为时间戳而为指定格式
     */
    @Bean
    public MappingJackson2HttpMessageConverter zsMappingJackson2HttpMessageConverter(
            @Autowired
                    ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                new MappingJackson2HttpMessageConverter();

        //设置返回值保留null
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //设置返回date的json格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        return mappingJackson2HttpMessageConverter;
    }
}
