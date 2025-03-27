package com.ll.nbe344team7.global.security.jwt;

import com.ll.nbe344team7.global.security.dto.CustomUserData;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * jwt 를 검증 및 CustomUserDetails 저장하는  클래스
 *
 * @since 2025-03-26
 * @author 이광석
 */
public class JWTFilter extends OncePerRequestFilter {

    final private JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Jwt를 검증 및 CustomUserDetails 저장하는 메소드
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     *
     * @since 2025-03-26
     * @author 이광석
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      //로그인 시에는 검증할 필요 없음
       if("/api/auth/login".equals(request.getServletPath())){
           filterChain.doFilter(request,response);
           return;
       }

        Cookie[] cookies = request.getCookies();

        if(cookies==null){
            filterChain.doFilter(request, response);
            return;
        }

        String Token="";

        //Cookie 중 accesToken을 찾음
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


        CustomUserData customUserData = new CustomUserData(memberId,username,role,"tmp");

        CustomUserDetails customUserDetails = new CustomUserDetails(customUserData);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request,response);

    }
}
