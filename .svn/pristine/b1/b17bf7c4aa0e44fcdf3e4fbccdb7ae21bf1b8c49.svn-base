package com.github.wxiaoqi.security.jd.sys;

import com.ace.cache.EnableAceCache;
import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import com.github.wxiaoqi.security.auth.client.EnableAceAuthClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableFeignClients({"com.github.wxiaoqi.security.auth.client.feign", "com.github.wxiaoqi.security.jd.sys.feign"})
//开启服务鉴权
@EnableAceAuthClient
@EnableAceCache
//开启分布式事务
@EnableDistributedTransaction
@EnableTransactionManagement
@MapperScan("com.github.wxiaoqi.security.jd.sys.mapper")
//@EnableSwagger2Doc
public class JDSysApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(JDSysApplication.class).run(args);
    }

}
