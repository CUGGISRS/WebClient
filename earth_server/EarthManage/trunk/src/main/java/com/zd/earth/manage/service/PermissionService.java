package com.zd.earth.manage.service;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zd.earth.manage.biz.UserBiz;
import com.zd.earth.manage.entity.User;
import com.zd.earth.manage.util.JwtUtils;
import com.zd.earth.manage.util.StringHelper;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 登陆权限
 */
@Service
public class PermissionService {


    @Value("${jwt.rsa-secret}")
    private String rsaSecret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.subject}")
    private String subject;

    @Value("${jwt.expire}")
    private long expire;

    /**
     * token存储的请求体键值
     */
    @Value("${jwt.token-header}")
    private String tokenHeader;

    @Autowired
    private UserBiz userBiz;



    /**
     * 根据用户信息生成token
     */
    public String createToken(Map<String,Object> jwtUser) {
        return JwtUtils.createJwtToken(jwtUser, issuer, subject, expire, rsaSecret);
    }


    /**
     * 登陆验证
     */
    public Map<String,Object> validate(String username, String password) {
        if (StringHelper.noNullOrEmpty(password)) {
            User user = userBiz.getOneByUsername(username);
            if (user != null) {
                //只有正常状态下的用户才能登陆
                //加密    if (Integer.valueOf(1).equals(user.getStatus())&&MD5Util.string2MD5(password).equals(MD5Util.convertMD5(user.getPassword()))) {
                if (Integer.valueOf(1).equals(user.getStatus()) && password.equals(user.getPassword())) {

                    //存入用户信息
                    Map<String,Object> jwtUser=new HashMap<>();
                    jwtUser.put("id",user.getId());
                    jwtUser.put("username",username);
                    jwtUser.put("name",user.getName());
                    jwtUser.put("type",user.getType());
                  //  BeanUtils.copyProperties(user, jwtUser);
                    return jwtUser;
                }
            }
        }
        return null;
    }



    /**
     * 通过token获得用户信息
     */
    public Map<String,Object> getUserInfo(String token) {
        return JwtUtils.parseJWT(token, rsaSecret);
    }


    /**
     * 验证token是否过期 true代表没过期
     */
    public boolean isTokenExpired(String token) {
        return JwtUtils.isTokenExpired(JwtUtils.parseJWT(token, rsaSecret));
    }


    /**
     * 获得请求中token
     */
    public String getCurrentToken(HttpServletRequest request)  {
       return request.getHeader(tokenHeader);
    }
}
