package com.zd.earth.manage.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;

import java.util.Date;
import java.util.Map;

/**
 * jwt工具类
 */
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
    public static String createJwtToken( Map<String, Object> jwtUser, String issuer, String subject, long expire, String secret) {

        // 生成签发时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 签名算法 ，将对token进行签名
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 通过秘钥签名JWT(区分不同服务的token)
        String key = TextCodec.BASE64.encode(secret);

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .setClaims(jwtUser)
                .signWith(signatureAlgorithm, key);

        if (expire >= 0) {
            long expMillis = nowMillis + expire;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();

    }

    /**
     * 解析token,获得token中的用户信息
     */
    public static Claims parseJWT(String token, String secret) {
        String k = TextCodec.BASE64.encode(secret);
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(k)
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
     * 验证token的有效期 true代表没过期
     */
    public static Boolean isTokenExpired( Claims body) {
        if (body != null) {
            final Date expiration = body.getExpiration();
            return expiration.after(new Date());
        }
        return false;
    }
}
