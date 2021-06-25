package com.github.wxiaoqi.security.dzsw.sys.annotation;

import java.lang.annotation.*;

/**
 * @author TsaiJun
 * 自定义注解，根据请求中的Authorization获取token，然后获取登录的用户信息。
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserToken {

}
