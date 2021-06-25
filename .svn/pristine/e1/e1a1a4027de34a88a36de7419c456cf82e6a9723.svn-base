package com.zd.earth.manage.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.text.SimpleDateFormat;

@Configuration
@Data
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    /**
     * 文件存放路径
     */
    @Value("${file.savePath}")
    private  String savePath;

    /**
     * 文件访问路径前缀
     */
    @Value("${file.visitPrefix}")
    private  String visitPrefix;

    /**
     * 服务名称
     */
    @Value("${spring.application.name}")
    private String serverName;
    /**
     * 临时文件的存储路径的父路径
     */
    @Value("${tmpParentPath}")
    private String tmpParentPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(visitPrefix+"**").addResourceLocations("file:"+savePath)
                .setCachePeriod(3600);//设置静态文件缓存  单位秒   ，不设则不缓存
    }
    /**
     * 改变临时文件的存储路径,防止自动清理临时文件时导致formData传参出现The temporary upload location xxx is not valid
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location =tmpParentPath+serverName;
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }

    /**
     * 配置全局json日期装换器
     * @return
     */
    @Bean
    public Jackson2ObjectMapperFactoryBean jackson2ObjectMapperFactoryBean(
            @Autowired
                    JsonDateConverter dateJacksonConverter) {
        Jackson2ObjectMapperFactoryBean jackson2ObjectMapperFactoryBean = new Jackson2ObjectMapperFactoryBean();

        jackson2ObjectMapperFactoryBean.setDeserializers(dateJacksonConverter);
        return jackson2ObjectMapperFactoryBean;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(
            @Autowired
                    ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                new MappingJackson2HttpMessageConverter();
        //设置返回date的json格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        return mappingJackson2HttpMessageConverter;
    }
}
