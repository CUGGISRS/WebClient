package com.github.wxiaoqi.security.gate.filter;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.api.vo.authority.PermissionInfo;
import com.github.wxiaoqi.security.api.vo.log.LogInfo;
import com.github.wxiaoqi.security.auth.client.config.ServiceAuthConfig;
import com.github.wxiaoqi.security.auth.client.config.UserAuthConfig;
import com.github.wxiaoqi.security.auth.client.jwt.ServiceAuthUtil;
import com.github.wxiaoqi.security.auth.client.jwt.UserAuthUtil;
import com.github.wxiaoqi.security.auth.common.util.jwt.IJWTInfo;
import com.github.wxiaoqi.security.common.constant.CommonConstants;
import com.github.wxiaoqi.security.common.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.msg.BaseResponse;
import com.github.wxiaoqi.security.common.msg.auth.TokenForbiddenResponse;
import com.github.wxiaoqi.security.common.util.StringHelper;
import com.github.wxiaoqi.security.common.vo.UserInfo;
import com.github.wxiaoqi.security.gate.feign.service.ILoginRegService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *访问过滤器（验证token）
 */
@Slf4j
@Configuration
@RefreshScope
public class VisitGatewayFilter implements GlobalFilter {
    @Autowired
    private ILoginRegService loginRegService;


    @Value("${gate.ignore.startWith}")
    private String startWith;


    private static final String GATE_WAY_PREFIX = "/api";

    @Autowired
    private ServiceAuthConfig serviceAuthConfig;

    @Autowired
    private UserAuthConfig userAuthConfig;

    @Autowired
    private ServiceAuthUtil serviceAuthUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
        log.info("check token....");
        LinkedHashSet requiredAttribute = serverWebExchange.getRequiredAttribute(
                ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        ServerHttpRequest request = serverWebExchange.getRequest();

        String requestUri = request.getPath().pathWithinApplication().value();

        if (requiredAttribute != null) {
            Iterator<URI> iterator = requiredAttribute.iterator();
            while (iterator.hasNext()){
                URI next = iterator.next();
                String nextPath=next.getPath();
                if(nextPath.startsWith(GATE_WAY_PREFIX)){
                    requestUri =nextPath.substring(GATE_WAY_PREFIX.length());
                }
            }
        }
        BaseContextHandler.setToken(null);
        ServerHttpRequest.Builder mutate = request.mutate();
        // 不进行拦截的地址
        if (isStartWith(requestUri)) {
            ServerHttpRequest build = mutate.build();
            return gatewayFilterChain.filter(serverWebExchange.mutate().request(build).build());
        }
        UserInfo user = null;
        try {
            user = getJWTUser(request, mutate);

            BaseContextHandler.set(CommonConstants.CONTEXT_KEY_USER_INFO,user);
        } catch (Exception e) {
            return getVoidMono(serverWebExchange, new TokenForbiddenResponse(e.getMessage()));
        }
        // 申请客户端密钥头
        mutate.header(serviceAuthConfig.getTokenHeader(), serviceAuthUtil.getClientToken());
        ServerHttpRequest build = mutate.build();
        return gatewayFilterChain.filter(serverWebExchange.mutate().request(build).build());

    }

    /**
     * 返回session中的用户信息
     */
    private UserInfo getJWTUser(ServerHttpRequest request, ServerHttpRequest.Builder ctx) throws Exception {
        String key=userAuthConfig.getTokenHeader();
        List<String> strings = request.getHeaders().get(key);
        String authToken = null;
        if (strings != null) {
            authToken = strings.get(0);
        }
        if(StringHelper.isNullOrEmpty(authToken)){
            throw new Exception("token不存在或为空！");
        }
        boolean isExpired=loginRegService.isTokenExpired(authToken);
        if(isExpired){
            ctx.header(key, authToken);
            BaseContextHandler.setToken(authToken);
            UserInfo userInfo=loginRegService.getUserByToken(authToken);
            if(userInfo!=null){
                return userInfo;
            }
        }
        throw new Exception("token解析失败或过期了！");
    }


    /**
     * 网关抛异常
     *
     * @param body
     */
    @NotNull
    private Mono<Void> getVoidMono(ServerWebExchange serverWebExchange, BaseResponse body) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        byte[] bytes = JSONObject.toJSONString(body).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(bytes);
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }




    /**
     * URI是否以什么打头
     */
    private boolean isStartWith(String requestUri) {
        for (String s : startWith.split(",")) {
            if (requestUri.startsWith(s)) {
                return true;
            }
        }
        return false;
    }


}
