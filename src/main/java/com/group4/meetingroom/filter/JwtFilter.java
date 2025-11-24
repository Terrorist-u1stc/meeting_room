package com.group4.meetingroom.filter;
import com.group4.meetingroom.entity.CustomUserDetails;
import com.group4.meetingroom.service.MyUserDetailService;
import com.group4.meetingroom.util.JwtUtils;
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
                Integer userId = JwtUtils.parseToken(token);
                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    CustomUserDetails userDetails = (CustomUserDetails) userDetailService.loadUserById(userId);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // token 无效或过期，可记录日志
            }
        }

        filterChain.doFilter(request, response);
    }
}

