package com.group4.meetingroom.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtils {
    private static final long EXPIRATION = 2 * 60 * 60 * 1000; // 2小时
    private static final String SECRET = "xK94mki2Nsa8eLz!@#QpRsTuVwXyZ123456";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public static String generateToken(Integer userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(KEY)
                .compact();
    }
    public static Integer parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return Integer.parseInt(claims.getSubject()); // 从 subject 里取出 userId

        } catch (ExpiredJwtException e) {
            System.out.println("Token 已过期");
        } catch (JwtException e) {
            System.out.println("Token 无效或签名错误：" + e.getMessage());
        } catch (Exception e) {
            System.out.println("解析 Token 时出错：" + e.getMessage());
        }
        return null;
    }
}
