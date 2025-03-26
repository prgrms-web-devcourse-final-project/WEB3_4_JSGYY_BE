package com.ll.nbe344team7.global.security.jwt;

import com.ll.nbe344team7.domain.member.entity.MemberEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    final private JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();

        String Token="";

        for(Cookie cookie : cookies){
            if("accessToken".equals(cookie.getName())){
                Token = cookie.getValue();
                break;
            }
        }

        if(Token.isEmpty() || jwtUtil.isExpired(Token)){
            filterChain.doFilter(request,response);
            return;
        }

        String username = jwtUtil.getUsername(Token);
        Long memberId= jwtUtil.getMemberId(Token);
        String role = jwtUtil.getRole(Token);



    }
}
