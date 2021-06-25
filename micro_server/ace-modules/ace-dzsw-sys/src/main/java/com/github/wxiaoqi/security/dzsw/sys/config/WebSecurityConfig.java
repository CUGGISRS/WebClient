package com.github.wxiaoqi.security.dzsw.sys.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * Spring security的总配置类
 * 拦截的url、登录接口地址、登录成功与失败后的处理器、各种异常处理器
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //token 过滤器，解析token
    @Autowired
    MyJwtTokenFilter jwtTokenFilter;
    //权限不足处理类
    @Autowired
    MyAccessDeniedHandler myAccessDeniedHandler;
    //其他异常处理类
    @Autowired
    MyAuthenticationException myAuthenticationException;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.
                //指定登录的地址，用户登录请求必须是表单格式，application/json方式是接受不到参数的
                //这样写参数可以 http://localhost:8080/user/login?username=admin&password=123456
                        formLogin()
                .and()
                //如果SpringBoot项目已经解决了跨域，那么只需要下面一行配置就可解决SpringSecurity跨域问题
                .cors().and()
                // 由于使用的是JWT，我们这里不需要csrf
                //关闭跨站请求防护
                .csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                // 需要验证token的请求写下面
                .antMatchers(
                        HttpMethod.GET,
                        "/user/getCurrentUser"
                ).authenticated()
                .anyRequest().permitAll(); // 匹配任意url
//                // 允许对于网站静态资源的无授权访问
//                .antMatchers(
//                        HttpMethod.GET,
//                        "/static/",
//                        "/static/apidoc/**", "/file/**", "/*.ico",
//                        "/static/html/**",
//                        "/static/**/*.css",
//                        "/static/**/*.js"
//                ).permitAll()
////                .antMatchers(
////                        HttpMethod.POST,
////                        "/qa/**"
////                ).permitAll()
//                .antMatchers(HttpMethod.HEAD, "/file/**").permitAll()
//                .antMatchers("/BhPersonInfo/**", "/BhOutsidePersonInfo/**", "/BhOutsideVehicleInfo/**").permitAll()
//                // 对于获取token的rest api要允许匿名访问
//                .antMatchers(HttpMethod.POST, "/user/login").permitAll()
//                // 除上面外的所有请求全部需要鉴权认证
//                .anyRequest().authenticated();

        // 禁用缓存
        httpSecurity.headers().cacheControl();

        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtTokenFilter, LogoutFilter.class)
                // 添加权限不足 filter
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler)
                //其他异常处理类
                .authenticationEntryPoint(myAuthenticationException);
    }
}
