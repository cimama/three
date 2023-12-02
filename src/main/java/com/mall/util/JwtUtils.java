package com.mall.util;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static final String jwtToken = "lang0913";

    public static String createToken(String phone){
        Map<String,Object> claims = new HashMap<>();
        claims.put("phone",phone);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtToken) // 签发算法，秘钥为jwtToken
                .setClaims(claims) // body数据，要唯一，自行设置
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 24L * 60 * 60 * 60 * 1000));// 一天的有效时间
        return jwtBuilder.compact();
    }

    public static Map<String, Object> checkToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public static void main(String[] args) {
        String token=JwtUtils.createToken("13791453657");
        System.out.println("token = " + token);
        Map<String,Object> map=JwtUtils.checkToken(token);
        if (map != null) {
            System.out.println("map = " + map.get("phone"));
        }
    }

}
