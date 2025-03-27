package com.ll.nbe344team7.global.security.controller;


import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.security.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReissueController {
    final private JWTUtil jwtUtil;

    public ReissueController(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }


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

        String newAccessToken = jwtUtil.createJwt("access",username,memberId,role,600000L);

        response.setHeader("access",newAccessToken);

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
