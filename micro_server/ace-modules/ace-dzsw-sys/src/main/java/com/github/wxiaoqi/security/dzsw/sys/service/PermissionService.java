package com.github.wxiaoqi.security.dzsw.sys.service;

import com.github.wxiaoqi.security.dzsw.sys.biz.UserBiz;
import com.github.wxiaoqi.security.dzsw.sys.entity.User;
import com.github.wxiaoqi.security.dzsw.sys.utils.JwtUtils;
import com.github.wxiaoqi.security.dzsw.sys.vo.JwtUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Autowired
    private UserBiz baseUserBiz;

    /**
     * 根据用户信息生成token
     *
     * @param jwtUser
     * @return
     */
    public String createToken(JwtUser jwtUser) {
        return JwtUtils.createJwtToken(jwtUser, issuer, subject, expire, rsaSecret);
    }

    //  private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    /**
     * 登陆验证
     *
     * @param username
     * @param password
     * @return
     */
    public JwtUser validate(String username, String password) {
        User user = baseUserBiz.selectOneByUsername(username);
        if (user != null) {
            //     if (encoder.matches(password, user.getPassword())) {
            if (password != null && password.equals(user.getPassword())) {
                JwtUser jwtUser = new JwtUser();
                BeanUtils.copyProperties(user, jwtUser);
                return jwtUser;
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
    public JwtUser getUserInfo(String token) {
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
