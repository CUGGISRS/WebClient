package com.github.wxiaoqi.security.com.sys.rest.service;

import com.github.wxiaoqi.security.auth.common.util.jwt.JWTHelper;
import com.github.wxiaoqi.security.com.sys.biz.UserBiz;
import com.github.wxiaoqi.security.com.sys.entity.User;
import com.github.wxiaoqi.security.com.sys.feign.service.IEnterpriseService;
import com.github.wxiaoqi.security.com.sys.util.JwtUtils;
import com.github.wxiaoqi.security.common.exception.auth.UserTokenException;
import com.github.wxiaoqi.security.common.util.MD5Util;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.vo.UserInfo;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;


@Service
@RefreshScope
public class PermissionService {


    @Value("${jwt.rsa-secret}")
    private String rsaSecret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.subject}")
    private String subject;

    @Value("${jwt.expire}")
    private long expire;
    @Autowired
    private UserBiz userBiz;

    @Autowired
    private IEnterpriseService enterpriseService;


    /**
     * 根据用户信息生成token
     *
     * @param jwtUser
     * @return
     */
    public String createToken(UserInfo jwtUser) {
        return JwtUtils.createJwtToken(jwtUser, issuer, subject, expire, rsaSecret);
    }


    /**
     * 登陆验证
     */
    public UserInfo validate(String username, String password, String sysName) {
        if (MyObjectUtil.noNullOrEmpty(password)) {
            User user = userBiz.getOneByUserNameAndSysName(username, sysName);
            if (user != null) {
                //只有正常状态下的用户才能登陆
                //加密    if (Integer.valueOf(1).equals(user.getStatus())&&MD5Util.string2MD5(password).equals(MD5Util.convertMD5(user.getPassword()))) {
                if (Integer.valueOf(1).equals(user.getStatus()) && password.equals(user.getPassword())) {
                    //获得存入企业信息的bean
                    UserInfo jwtUser = enterpriseService.getOneByUserId(user.getId());
                    //存入用户信息
                    BeanUtils.copyProperties(user, jwtUser);
                    return jwtUser;
                }
            }
        }
        return null;
    }


    /**
     * 通过token获得用户信息
     *
     * @return
     * @throws Exception
     */
    public UserInfo getUserInfo(String token) {
        return JwtUtils.getInfoFromToken(token, rsaSecret);
    }


    /**
     * 验证token是否过期
     *
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        return JwtUtils.isTokenExpired(token, rsaSecret);
    }
}
