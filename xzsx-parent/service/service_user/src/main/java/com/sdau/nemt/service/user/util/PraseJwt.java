package com.sdau.nemt.service.user.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import java.text.SimpleDateFormat;

/**
 * @author
 * @since 2023-08-11
 * @Description: 解析token
 */
public class PraseJwt {
    public static void tokenToOut(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey("my-123")
                .parseClaimsJws(token)
                .getBody();
        System.out.println("用户id:"+claims.getId());
        System.out.println("用户名:"+claims.getSubject());
        System.out.println("用户时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                format(claims.getIssuedAt()));System.out.println("过期时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                format(claims.getExpiration()));
        System.out.println("用户角色:"+claims.get("role"));
    }
}
