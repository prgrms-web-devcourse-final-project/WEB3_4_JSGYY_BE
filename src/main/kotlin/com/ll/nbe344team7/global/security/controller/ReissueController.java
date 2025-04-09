package com.ll.nbe344team7.global.security.controller;


import com.ll.nbe344team7.global.redis.RedisRepository;
import com.ll.nbe344team7.global.security.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * access token 재발급
 *
 * @author 이광석
 * @since 2025-03-27
 */
@Tag(name = "토큰 재발급 API")
@RestController
public class ReissueController {
    final private JWTUtil jwtUtil;
    final private RedisRepository redisRepository;

    private static final Long ACCESS_TOKEN_EXPIRY =  60 * 10 * 1000L;

    public ReissueController(JWTUtil jwtUtil , RedisRepository redisRepository){
        this.jwtUtil = jwtUtil;
        this.redisRepository = redisRepository;
    }


    /**
     * accessToken이 만료되었을경우 refresh 토큰을 이용하여  새로운 accessToken을 재발급
     * 새로운 accessToken을 redis에 저장
     * @param request
     * @param response
     * @return ResponseEntity<?>
     * @author 이광석
     * @since 2025-03-27
     */
    @Operation(summary = "accessToken 만료 시 refreshToken을 이용하여 accessToken 재발급")
    @PostMapping("/api/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
            }
        }

        if(refresh ==null){
            return ResponseEntity.badRequest().body("refresh token null");
        }

        try{
            jwtUtil.isExpired(refresh);
        }catch (ExpiredJwtException e){
            return ResponseEntity.badRequest().body("token expired");
        }

        if(!jwtUtil.getCategory(refresh).equals("refresh")){
            return ResponseEntity.badRequest().body("invalid refresh token");
        }

        String username = jwtUtil.getUsername(refresh);
        Long memberId = jwtUtil.getMemberId(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccessToken = jwtUtil.createJwt("access",username,memberId,role,60*10*1000L);

        redisRepository.modify(refresh,newAccessToken);

        response.setHeader("access",newAccessToken);

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
