package com.github.wxiaoqi.security.gate.filter;

import com.github.wxiaoqi.security.api.vo.log.LogInfo;
import com.github.wxiaoqi.security.gate.utils.DBLog;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
//@Component
/*
public class ModifyResponseFilter implements GlobalFilter , Ordered{
    @Autowired
    @Lazy
    private ILogService logService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        try {
            //获取response的 返回数据
            ServerHttpResponse originalResponse = exchange.getResponse();

            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            //最重要的是这里，网上有很多对gateway响应进行修改的,但是我这里会进行截断（并不是每次都截）,刚开始也是按照buffer一个一个读，
                            // 然后看了下api 发现可以获取出所有流的集合, 然后又继续查api 发现  DataBufferFactory 可以进行合并多个流的集合,然后就把这个问题解决了。
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer join = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[join.readableByteCount()];
                            join.read(content);
                            //释放掉内存
                            DataBufferUtils.release(join);
                            String responseData = new String(content, StandardCharsets.UTF_8);
      //                      System.out.println("响应内容:{}"+responseData);
                            //补充日志存入数据库
                            LogInfo logInfo=(LogInfo) exchange.getAttributes().get("logInfo");
                            if(logInfo!=null){
                                logInfo.setResponseInfo(responseData);
                                DBLog.getInstance().setLogService(logService).offerQueue(logInfo);
                            }
                          return bufferFactory.wrap(content);
                        }));
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);
        } catch (Exception e) {
            System.out.println(" ModifyResponseFilter 異常"+e);
            return chain.filter(exchange);
        }

    }


    @Override
    public int getOrder() {
        return -1;
    }
}
*/
