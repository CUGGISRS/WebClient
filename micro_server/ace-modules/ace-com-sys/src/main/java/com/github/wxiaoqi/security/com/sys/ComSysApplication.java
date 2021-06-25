package com.github.wxiaoqi.security.com.sys;

import com.ace.cache.EnableAceCache;
import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import com.github.wxiaoqi.security.auth.client.EnableAceAuthClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableFeignClients({"com.github.wxiaoqi.security.auth.client.feign", "com.github.wxiaoqi.security.com.sys.feign"})
//开启服务鉴权
@EnableAceAuthClient
@EnableAceCache
@MapperScan("com.github.wxiaoqi.security.com.sys.mapper")
//@EnableSwagger2Doc

//开启分布式事务
@EnableDistributedTransaction
@EnableTransactionManagement
public class ComSysApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ComSysApplication.class).run(args);
    }

}
