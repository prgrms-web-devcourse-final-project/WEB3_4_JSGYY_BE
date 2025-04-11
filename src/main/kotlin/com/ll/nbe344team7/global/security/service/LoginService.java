package com.ll.nbe344team7.global.security.service;

import com.ll.nbe344team7.global.redis.RedisRepository;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import com.ll.nbe344team7.global.security.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final JWTUtil jwtUtil;
    private final RedisRepository redisRepository;



    public LoginService(JWTUtil jwtUtil, RedisRepository redisRepository) {
        this.jwtUtil = jwtUtil;
        this.redisRepository = redisRepository;
    }

    public void generateAndAttachTokens(HttpServletResponse response, CustomUserDetails userDetails) {
        String accessToken = jwtUtil.createJwt("access", userDetails.getUsername(), userDetails.getMemberId(), userDetails.getNickname(), userDetails.getRole(), 1);

        String refreshToken = jwtUtil.createJwt("refresh", userDetails.getUsername(), userDetails.getMemberId(), userDetails.getNickname(), userDetails.getRole(), 2);

        redisRepository.save(refreshToken, accessToken, 60 * 60 * 24L);

        // refresh 토큰은 쿠키로
        Cookie cookie = new Cookie("refresh", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        // access 토큰은 헤더로
        response.setHeader("access", accessToken);
    }
}
