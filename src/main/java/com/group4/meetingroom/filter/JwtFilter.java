package com.group4.meetingroom.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.meetingroom.entity.CustomUserDetails;
import com.group4.meetingroom.service.MyUserDetailService;
import com.group4.meetingroom.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final MyUserDetailService userDetailService;

    public JwtFilter(MyUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {

                Claims claims = JwtUtils.parseClaims(token);
                Integer userId = Integer.valueOf(claims.getSubject());
                Integer tokenVersion = claims.get("tokenVersion", Integer.class);



                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    System.out.println("Authentication was NULL, setting a new authentication.");

                    CustomUserDetails userDetails = (CustomUserDetails) userDetailService.loadUserById(userId);
                    if (!userDetails.getTokenVersion().equals(tokenVersion)) {
                        throw new RuntimeException("登录已失效，请重新登录");
                    }

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                System.out.println("解析 token 失败 => " + e.getMessage());
                SecurityContextHolder.clearContext();
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(
                        new ObjectMapper().writeValueAsString(
                                Map.of("code", 401, "message", e.getMessage())
                        )
                );
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}