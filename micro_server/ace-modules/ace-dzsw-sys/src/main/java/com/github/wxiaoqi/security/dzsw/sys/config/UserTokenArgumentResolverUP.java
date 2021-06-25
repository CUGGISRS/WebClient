package com.github.wxiaoqi.security.dzsw.sys.config;

import com.github.wxiaoqi.security.common.exception.auth.UserTokenException;
import com.github.wxiaoqi.security.dzsw.sys.annotation.UserToken;
import com.github.wxiaoqi.security.dzsw.sys.service.PermissionService;
import com.github.wxiaoqi.security.dzsw.sys.vo.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author TsaiJun
 * 解析器，从请求头中的token中获取UserInfo
 */
@Component
public class UserTokenArgumentResolverUP implements HandlerMethodArgumentResolver {

    @Value("${jwt.token-header}")
    private String tokenHeader;

    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //如果函数包含我们的自定义注解，那就走下面的resolveArgument()函数
        if (methodParameter.hasParameterAnnotation(UserToken.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        //获取请求中token值
        String authToken = request.getHeader(tokenHeader);
        if (authToken != null) {
            try {
                //根据token获取当前登录的用户信息
                JwtUser user = permissionService.getUserInfo(authToken);
                return user;
            } catch (Exception e) {
                //token值为空或者有误
                throw new UserTokenException("User token is null or empty!");
            }
        } else {
            return new UserTokenException("User token is null!");
        }
    }
}
