package com.sdau.nemt.service.user.util;

import com.sdau.nemt.service.user.entity.vo.InfoVO;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author
 * @since 2023-08-11
 * @Description: 生成 token
 */
public class CreateJwt {
    public static String  getoken(InfoVO user) {
        //Jwts.builder()生成
        //Jwts.parser()验证
        JwtBuilder jwtBuilder =  Jwts.builder()
                .setId(user.getId()+"")
                .setSubject(user.getUsername())    //用户名
                .setIssuedAt(new Date())//登录时间
                .signWith(SignatureAlgorithm.HS256, "my-123").setExpiration(new Date(System.currentTimeMillis()+86400000));
        //设置过期时间
        //前三个为载荷playload 最后一个为头部 header
        System.out.println(jwtBuilder.compact());
        return  jwtBuilder.compact();
    }
}
