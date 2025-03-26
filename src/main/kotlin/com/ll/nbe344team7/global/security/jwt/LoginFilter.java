package com.ll.nbe344team7.global.security.jwt;

import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;


/**
 * UsernamePasswordAuthenticationFilter 상속
 * 입력 받은 데이터를 authenticationManager에게 전달
 * 로그인 성공 : jwt 발급
 * 로그인 실패 : badRequest 전달
 * @since 2025-03-26
 * @author 이광석
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUil;

    public LoginFilter(
            AuthenticationManager authenticationManager,
            JWTUtil jwtUil) {

        this.authenticationManager = authenticationManager;
        this.jwtUil = jwtUil;
    }

    /**
     * Requse에서 username, password 를 추출
     * username,password를 authenticationManager에 전달
     *
     * @param request
     * @param response
     * @return Authentication
     * @throws AuthenticationException
     * @since 2025-03-25
     * @author 이광석
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);


        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }


    /**
     * 로그인 성공 메서드
     * 로그인 성공시 사용자 정보를 이용해서 jwt토큰 생성
     * 해당 토큰을 담아 쿠키 생성
     * 쿠키를 클라이언트에 전달
     * @param request
     * @param response
     * @param chain
     * @param authentication
     *
     * @since 2025-03-26
     * @author 이광석
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomUserDetails customUserDetails= (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        Long memberId = customUserDetails.getMemberId();


        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();


        String token = jwtUil.createJwt(username,memberId,role,60*60*10L);

        Cookie cookie = new Cookie("accessToken",token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(600*60*10);
        response.addCookie(cookie);


    }

    /**
     * 로그인 실패 메서드
     * @param request
     * @param response
     * @param failed
     *
     * @since 2025-03-26
     * @author 이광석
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}