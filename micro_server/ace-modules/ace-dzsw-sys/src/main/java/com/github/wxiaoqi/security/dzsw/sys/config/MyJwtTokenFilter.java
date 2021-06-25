package com.github.wxiaoqi.security.dzsw.sys.config;

import com.github.wxiaoqi.security.dzsw.sys.service.PermissionService;
import com.github.wxiaoqi.security.dzsw.sys.vo.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyJwtTokenFilter extends OncePerRequestFilter {
    @Value("${jwt.token-header}")
    private String tokenHeader;

    @Value("${spring.servlet.multipart.max-request-size}")
    private String maxRequestSize;
    @Autowired
    private PermissionService permissionService;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String contentLength = httpServletRequest.getHeader("content-length");
        if (contentLength != null) {
            //判断请求大小是否超过限制
            long length = Long.parseLong(contentLength);
            if (checkMBSize(length, maxRequestSize)) {
                throw new IOException("请求过大");
            }
        }
        String authToken = httpServletRequest.getHeader(tokenHeader);
        if (authToken != null) {
            System.out.println("开始token验证");
            //验证token,具体怎么验证看需求，可以只验证token不查库，把权限放在jwt中即可
            if (permissionService.isTokenExpired(authToken)) {
                JwtUser user = permissionService.getUserInfo(authToken);
//                JwtUser user = new JwtUser();
                //刷新token存入请求头
             /*   String newToken=permissionService.createToken(user);
                httpServletRequest.setAttribute(tokenHeader,newToken);*/

     /*   JwtUser user=new JwtUser();
        user.setId(20);
        user.setUsername("test");
        user.setName("xxx");*/
                //这里只要告诉springsecurity权限即可，账户密码就不用提供验证了
                // 这里直接传user对象传给UsernamePasswordAuthenticationToken，而不是username和password，方便以后直接获取当前用户信息
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                logger.info(String.format("Authenticated userDetail %s, setting security context", user.getUsername()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * 比较MB单位的字符串大小
     *
     * @return
     */
    private boolean checkMBSize(Long len, String maxSize) {
        maxSize = maxSize.substring(0, maxSize.length() - 2);
        double size = Double.parseDouble(maxSize) * 1048576;
        if (len > size) {
            return true;
        }
        return false;
    }
}
