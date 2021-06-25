package com.zd.earth.manage.config;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger特有配置
 */
@Configuration
public class SwaggerConfig {
    /**
     * 扫描包位置
     */
    @Value("${swagger.base-package}")
    private String packagePath;
    /**
     * 标题
     */
    @Value("${swagger.title}")
    private String title;
    /**
     * 版本
     */
    @Value("${swagger.version}")
    private String version;
    /**
     * 描述
     */
    @Value("${swagger.description}")
    private String description;
    /**
     * 开发者名称
     */
    @Value("${swagger.contact.name}")
    private String contactName;
    /**
     * 开发者邮箱
     */
    @Value("${swagger.contact.email}")
    private String contactEmail;
    /**
     * 查看更多的路径
     */
    @Value("${swagger.contact.url}")
    private String contactUrl;
    /**
     * 公共请求头(string类型)
     */
    @Value("${swagger.commonHeader.name}")
    private String commonHeaderName;
    /**
     * 公共请求头描述
     */
    @Value("${swagger.commonHeader.description}")
    private String commonHeaderDescription;

    @Bean
    public Docket createRestApi() {
        //=====添加统一head参数，不一样的可以用 @ApiImplicitParam header 再添加====
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name(commonHeaderName).description(commonHeaderDescription).modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        // =========添加head参数end===================
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(packagePath))//扫描包位置
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))//只扫描带该注解的接口
              //  .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .contact(new Contact(contactName, contactUrl, contactEmail))
                .version(version)
                .build();
    }
}
