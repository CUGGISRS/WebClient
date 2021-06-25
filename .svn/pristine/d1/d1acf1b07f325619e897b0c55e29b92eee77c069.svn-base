package com.github.wxiaoqi.security.dzsw.sys;

import com.ace.cache.EnableAceCache;
import com.github.wxiaoqi.security.auth.client.EnableAceAuthClient;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients({"com.github.wxiaoqi.security.auth.client.feign"})
@EnableScheduling
@EnableAceAuthClient
@EnableAceCache
@EnableTransactionManagement

@SpringBootApplication
@MapperScan("com.github.wxiaoqi.security.dzsw.sys.mapper")
@EnableSwagger2Doc
public class dzswSystemApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(dzswSystemApplication.class).run(args);
    }
}
