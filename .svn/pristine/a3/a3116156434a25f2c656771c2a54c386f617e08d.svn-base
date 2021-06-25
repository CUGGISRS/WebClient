package com.github.wxiaoqi.security.dzsw.sys.utils;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.util.StringHelper;
import com.github.wxiaoqi.security.dzsw.sys.constants.CommonConstants;
import com.github.wxiaoqi.security.dzsw.sys.vo.JwtUser;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    /**
     * 生成Token
     *
     * @param jwtUser 用户信息
     * @param issuer  该JWT的签发者，是否使用是可选的
     * @param subject 该JWT所面向的用户，是否使用是可选的；
     * @param expire  签发时间  什么时候过期，这里是一个Unix时间戳
     * @param secret  私钥
     * @return token String
     */
    public static String createJwtToken(JwtUser jwtUser, String issuer, String subject, long expire, String secret) {

        // 签名算法 ，将对token进行签名
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 生成签发时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 通过秘钥签名JWT
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap();
        //转化为json字符串存储
        claims.put(CommonConstants.JWT_KEY_USER, JSONObject.toJSONString(jwtUser));

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .setClaims(claims)
                .signWith(signatureAlgorithm, signingKey);


        if (expire >= 0) {
            long expMillis = nowMillis + expire;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();

    }

    /**
     * 解析token获得Claims
     *
     * @param token
     * @param secret
     * @return
     */
    public static Claims parseJWT(String token, String secret) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                    .parseClaimsJws(token).getBody();
            return claims;
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
            System.out.println("token过期了 " + token);
            return claims;
        } catch (Exception exception) {
            System.out.println("token解析失败 " + token);
            return null;
        }
    }

    /**
     * 获取token中的用户信息
     *
     * @param token
     * @param secret
     * @return
     * @throws Exception
     */
    public static JwtUser getInfoFromToken(String token, String secret) {
        Claims body = parseJWT(token, secret);
        if (body == null) {
            return null;
        }
        return JSONObject.parseObject(StringHelper.getObjectValue(body.get(CommonConstants.JWT_KEY_USER)), JwtUser.class);
    }

    /**
     * 验证token的有效期 true代表没过期
     *
     * @param token
     * @param secret
     * @return
     */
    public static Boolean isTokenExpired(String token, String secret) {
        Claims body = parseJWT(token, secret);
        if (body != null) {
            final Date expiration = body.getExpiration();
            return expiration.after(new Date());
        }
        return false;
    }


}
