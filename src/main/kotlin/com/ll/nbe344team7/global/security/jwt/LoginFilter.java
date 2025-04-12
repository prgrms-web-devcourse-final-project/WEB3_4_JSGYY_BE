package com.ll.nbe344team7.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.nbe344team7.global.redis.RedisRepository;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


/**
 * UsernamePasswordAuthenticationFilter 상속
 * 입력 받은 데이터를 authenticationManager에게 전달
 * 로그인 성공 : jwt 발급
 * 로그인 실패 : badRequest 전달
 *
 * @author 이광석
 * @since 2025-03-26
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUil;

    private final RedisRepository redisRepository;

    public LoginFilter(
            AuthenticationManager authenticationManager,
            JWTUtil jwtUil,
            RedisRepository redisRepository) {

        this.authenticationManager = authenticationManager;
        this.jwtUil = jwtUil;
        this.redisRepository = redisRepository;
    }

    /**
     * Request에서 username, password 를 추출
     * username,password를 authenticationManager에 전달
     *
     * @param request
     * @param response
     * @return Authentication
     * @throws AuthenticationException
     * @author 이광석
     * @since 2025-03-25
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> requestMap = objectMapper.readValue(request.getInputStream(), Map.class);

            String username = requestMap.get("username");
            String password = requestMap.get("password");

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("회원 정보를 다시 확인해주세요");
        }
    }


    /**
     * 로그인 성공 메서드
     * 로그인 성공시 사용자 정보를 이용해서 jwt토큰 생성
     * 해당 토큰을 담아 쿠키 생성
     * 쿠키를 클라이언트에 전달
     *
     * @param request
     * @param response
     * @param chain
     * @param authentication
     * @author 이광석
     * @since 2025-03-26
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        Long memberId = customUserDetails.getMemberId();
        String nickname = customUserDetails.getNickname();


        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String accessToken = jwtUil.createJwt("access", username, memberId, nickname, role, 1);
        String refreshToken = jwtUil.createJwt("refresh", username, memberId, nickname, role, 2);

        redisRepository.save(refreshToken, accessToken, JWTUtil.refreshExpiration);

        String refreshCookieString = ResponseCookie
                .from("refresh", refreshToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build()
                .toString();

        response.addHeader("Set-Cookie", refreshCookieString);
        response.addHeader("access", accessToken);
        response.setStatus(HttpStatus.OK.value());
    }

    /**
     * 로그인 실패 메서드
     *
     * @param request
     * @param response
     * @param failed
     * @author 이광석
     * @since 2025-03-26
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}