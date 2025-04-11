package com.ll.nbe344team7.global.config.webSocket.handler.command;

import com.ll.nbe344team7.global.security.dto.CustomUserData;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import com.ll.nbe344team7.global.security.exception.SecurityException;
import com.ll.nbe344team7.global.security.exception.SecurityExceptionCode;
import com.ll.nbe344team7.global.security.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 웹 소캣 연결 명령
 *
 * @author jyson
 * @since 25. 4. 7.
 */
@Component
public class ConnectHandler implements StompCommandHandler{
    private final JWTUtil jwtUtil;

    public ConnectHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    public void handle(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        Cookie[] cookies = (Cookie[]) sessionAttributes.get("cookies");
        String token = "";

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        String username = jwtUtil.getUsername(token);
        Long memberId = jwtUtil.getMemberId(token);
        String role = jwtUtil.getRole(token);
        String nickname = jwtUtil.getNickname(token);

        CustomUserData customUserData = new CustomUserData(memberId, username, nickname, role, "tmp");
        CustomUserDetails customUserDetails = new CustomUserDetails(customUserData);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        accessor.setUser(authToken);

        if (token != null) {
            if (jwtUtil.isExpired(token)) {
                throw new SecurityException(SecurityExceptionCode.REFRESHTOKEN_IS_EXPIRED);
            }
        } else {
            throw new SecurityException(SecurityExceptionCode.NOT_FOUND_REFRESHTOKEN);
        }
    }
}
