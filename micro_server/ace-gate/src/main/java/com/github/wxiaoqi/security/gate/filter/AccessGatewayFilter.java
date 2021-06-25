package com.github.wxiaoqi.security.gate.filter;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.api.vo.authority.PermissionInfo;
import com.github.wxiaoqi.security.api.vo.log.LogInfo;
import com.github.wxiaoqi.security.auth.client.config.ServiceAuthConfig;
import com.github.wxiaoqi.security.auth.client.config.UserAuthConfig;
import com.github.wxiaoqi.security.auth.client.jwt.ServiceAuthUtil;
import com.github.wxiaoqi.security.auth.client.jwt.UserAuthUtil;
import com.github.wxiaoqi.security.auth.common.util.jwt.IJWTInfo;
import com.github.wxiaoqi.security.common.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.msg.BaseResponse;
import com.github.wxiaoqi.security.common.msg.auth.TokenForbiddenResponse;
import com.github.wxiaoqi.security.common.util.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ace(此处实现Ordered会报错)
 * @create 2018/3/12.
 */
@Slf4j
//@Configuration
public class AccessGatewayFilter implements GlobalFilter {
/*
    @Autowired
    @Lazy
    private IUserService userService;
*/


    @Value("${gate.ignore.startWith}")
    private String startWith;
    /**
     * 存储url内包含的参数
     */
    private String urlParam=null;

    private static final String GATE_WAY_PREFIX = "/api";
    @Autowired
    private UserAuthUtil userAuthUtil;

    @Autowired
    private ServiceAuthConfig serviceAuthConfig;

    @Autowired
    private UserAuthConfig userAuthConfig;

    @Autowired
    private ServiceAuthUtil serviceAuthUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
        log.info("check token and user permission....");
        LinkedHashSet requiredAttribute = serverWebExchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        ServerHttpRequest request = serverWebExchange.getRequest();

        String requestUri = request.getPath().pathWithinApplication().value();

        if (requiredAttribute != null) {
            Iterator<URI> iterator = requiredAttribute.iterator();
            while (iterator.hasNext()){
                URI next = iterator.next();
                if(next.getPath().startsWith(GATE_WAY_PREFIX)){
                    requestUri = next.getPath().substring(GATE_WAY_PREFIX.length());
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
       /* IJWTInfo user = null;
        try {
            user = getJWTUser(request, mutate);
        } catch (Exception e) {
            log.error("用户Token过期异常", e);
            return getVoidMono(serverWebExchange, new TokenForbiddenResponse("User Token Forbidden or Expired!"));
        }
        List<PermissionInfo> permissionIfs = userService.getAllPermissionInfo();

        // 判断资源是否启用权限约束
        Stream<PermissionInfo> stream = getPermissionIfs(requestUri, request.getMethodValue(), permissionIfs);
        List<PermissionInfo> result = stream.collect(Collectors.toList());
        PermissionInfo[] permissions = result.toArray(new PermissionInfo[]{});

        if (permissions.length > 0) {
            if (checkUserPermission(permissions, user,serverWebExchange)) {
                return getVoidMono(serverWebExchange, new TokenForbiddenResponse("User Forbidden!Does not has Permission!"));
            }
        }*/

        // 申请客户端密钥头
        mutate.header(serviceAuthConfig.getTokenHeader(), serviceAuthUtil.getClientToken());
        ServerHttpRequest build = mutate.build();
        return gatewayFilterChain.filter(serverWebExchange.mutate().request(build).build());

    }
/*    private boolean checkUserPermission(PermissionInfo[] permissions, IJWTInfo user,ServerWebExchange serverWebExchange) {
        List<PermissionInfo> permissionInfos = userService.getPermissionByUsername(user.getUniqueName());
        PermissionInfo current = null;
        for (PermissionInfo info : permissions) {
        //    System.out.println("参数"+info);
            boolean anyMatch = permissionInfos.parallelStream().anyMatch(new Predicate<PermissionInfo>() {
                @Override
                public boolean test(PermissionInfo permissionInfo) {
                    return permissionInfo.getCode().equals(info.getCode());
                }
            });
            if (anyMatch) {
                current = info;
                break;
            }
        }
        if (current == null) {
            return true;
        } else {
            //决定get请求日志是否记录
               if (!"GET".equals(current.getMethod())) {
            setCurrentUserInfoAndLog( user, current,serverWebExchange);
                 }
            return false;
        }
    }*/

    private void setCurrentUserInfoAndLog(IJWTInfo user, PermissionInfo pm,ServerWebExchange serverWebExchange) {
        //    System.out.println("3"+serverWebExchange.getAttribute("cachedRequestBodyObject"));
        ServerHttpRequest request = serverWebExchange.getRequest();
        String method = request.getMethodValue();
        //记录统一日志
        LogInfo logInfo=new LogInfo();
        if(urlParam!=null){
            logInfo.setUrlParam(urlParam);
            urlParam=null;
        }
        logInfo.setType(method);
        logInfo.setHeader(request.getHeaders().toString());
        //获得真实ip
        logInfo.setCrtHost(getIpAddress(request));
        if("POST".equals(method)||"PUT".equals(method)){
            //获取request body
            String bodyStr = serverWebExchange.getAttribute(CacheBodyGatewayFilter.CACHE_REQUEST_BODY_OBJECT_KEY);
            logInfo.setBody(bodyStr);
        }else {
            if(request.getQueryParams().size()>0){
                logInfo.setUrlParam(request.getQueryParams().toString());
            }
        }
        logInfo.setMenu(pm.getMenu());
        logInfo.setUri(pm.getUri());
        logInfo.setOpt(pm.getName());

        logInfo.setCrtTime(new Date());
        logInfo.setCrtUser(user.getId());
        logInfo.setCrtName(user.getName());
        //日志信息存入属性值，到响应过滤器中取出
        serverWebExchange.getAttributes().put("logInfo",logInfo);
    }

    /**
     * 获取目标权限资源
     *
     * @param requestUri
     * @param method
     * @param serviceInfo
     * @return
     */
    private Stream<PermissionInfo> getPermissionIfs(final String requestUri, final String method, List<PermissionInfo> serviceInfo) {
        return serviceInfo.parallelStream().filter(new Predicate<PermissionInfo>() {
            @Override
            public boolean test(PermissionInfo permissionInfo) {
                String uri = permissionInfo.getUri();

                String[] array=requestUri.split("/");
                boolean existParam=true;
                //判断uri中是否包含参数(数字)
                for(int i=0;i<array.length;i++){
                    //判断是否为整数
                    if(StringHelper.isInteger(array[i])){
                        //且get请求不赋值
                        if(!"GET".equals(method)){
                            urlParam=array[i];
                        }
                        existParam=false;
                        break;
                    }
                }
                if(existParam){
                    return uri.equals(requestUri)&&method.equals(permissionInfo.getMethod());
                }

                if (uri.indexOf("{") > 0) {
                    uri = uri.replaceAll("\\{\\*\\}", "[a-zA-Z\\\\d]+");
                }
                String regEx = "^" + uri + "$";
            //    System.out.println("3  "+Pattern.compile(regEx));
                return (Pattern.compile(regEx).matcher(requestUri).find())
                        && method.equals(permissionInfo.getMethod());
            }
        });
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
     * 返回session中的用户信息
     *
     * @param request
     * @param ctx
     * @return
     */
    private IJWTInfo getJWTUser(ServerHttpRequest request, ServerHttpRequest.Builder ctx) throws Exception {
        List<String> strings = request.getHeaders().get(userAuthConfig.getTokenHeader());
        String authToken = null;
        if (strings != null) {
            authToken = strings.get(0);
        }
        if (StringUtils.isBlank(authToken)) {
            strings = request.getQueryParams().get("token");
            if (strings != null) {
                authToken = strings.get(0);
            }
        }
        ctx.header(userAuthConfig.getTokenHeader(), authToken);
        BaseContextHandler.setToken(authToken);
        return userAuthUtil.getInfoFromToken(authToken);
    }




    /**
     * URI是否以什么打头
     *
     * @param requestUri
     * @return
     */
    private boolean isStartWith(String requestUri) {
        boolean flag = false;
        for (String s : startWith.split(",")) {
            if (requestUri.startsWith(s)) {
                return true;
            }
        }
        return flag;
    }

    /**
     * 网关抛异常
     *
     * @param body
     * @param code
     */
    private Mono<Void> setFailedRequest(ServerWebExchange serverWebExchange, String body, int code) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        return serverWebExchange.getResponse().setComplete();
    }


    /**
     * 获取真实ip的方法
     * @param request
     * @return
     */
    public static String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }

}
