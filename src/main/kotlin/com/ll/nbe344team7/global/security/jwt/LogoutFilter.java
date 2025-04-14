package com.ll.nbe344team7.global.security.jwt;

import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.service.MemberService;
import com.ll.nbe344team7.global.redis.RedisRepository;
import com.ll.nbe344team7.global.security.exception.SecurityException;
import com.ll.nbe344team7.global.security.exception.SecurityExceptionCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 로그아웃 필터
 */
public class LogoutFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final RedisRepository redisRepository;

    private final MemberService memberService;

    public LogoutFilter(JWTUtil jwtUtil, RedisRepository redisRepository, MemberService memberService) {
        this.jwtUtil = jwtUtil;
        this.redisRepository = redisRepository;
        this.memberService = memberService;
    }

    /**
     * 로그아웃 필터
     * 적절한 logout 요청인지 확인하고 redis 에서 해당 사용자의 토큰들을 지운 다음 빈 토큰을 쿠키에 담아 전달
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     * @author 이광석
     * @since 25.03.31
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 로그아웃 요청이 아닌 경우 필터 통과
        if (!request.getRequestURI().equals("/api/auth/logout") || !request.getMethod().equals("POST")
        || !request.getRequestURI().equals("/api/member/withdrawal")) {
            filterChain.doFilter(request, response);
            return;
        }

        if(request.getRequestURI().equals("/api/auth/logout")){
            memberService.withdrawal();
        }

        // 쿠키에서 refresh 토큰 꺼내기
        String refresh = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        // refresh 토큰 검증
        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("message : 리프레쉬 토큰이 없습니다.");
            throw new SecurityException(SecurityExceptionCode.NOT_FOUND_REFRESHTOKEN);
        }
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("message : ");
            throw new SecurityException(SecurityExceptionCode.REFRESHTOKEN_IS_EXPIRED);
        }

        if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("message : ");
            throw new SecurityException(SecurityExceptionCode.IS_NOT_REFRESHTOKEN);
        }

        // Redis에서 해당 refresh로 저장된 access 토큰 삭제
        String accessToken = redisRepository.get(refresh);
        if (accessToken == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new SecurityException(SecurityExceptionCode.NOT_FOUND_REFRESHTOKEN);
        }

        redisRepository.delete(refresh);

        String refreshCookieString = ResponseCookie
                .from("refresh", null)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(0)
                .build()
                .toString();

        response.addHeader("Set-Cookie", refreshCookieString);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
