package com.group4.meetingroom.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtils {
    private static final long EXPIRATION = 2 * 60 * 60 * 1000; // 2小时
    private static final String SECRET = "xK94mki2Nsa8eLz!@#QpRsTuVwXyZ123456";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public static String generateToken(Integer userId, Integer tokenVersion) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("tokenVersion", tokenVersion)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    public static Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("登录已过期");
        } catch (Exception e) {
            throw new RuntimeException("无效的令牌");
        }
    }

    public static Integer parseToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            return null;
        }
    }
}