package com.ll.nbe344team7.global.security.controller;

import com.ll.nbe344team7.global.security.dto.LoginDto;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import com.ll.nbe344team7.global.security.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 스웨거 전용 로그인
 *
 * @author kjm72
 * @since 2025-04-09
 */
@Tag(name = "스웨거 전용 로그인 API")
@RestController
@RequestMapping("/api")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final LoginService loginService;

    public LoginController(AuthenticationManager authenticationManager, LoginService loginService) {
        this.authenticationManager = authenticationManager;
        this.loginService = loginService;
    }

    /**
     *
     * @param loginDto
     * @param response
     * @return
     *
     * @author kjm72
     * @since 2025-04-09
     */
    @Operation(summary = "스웨거 전용 로그인", description = "로그인 후 response에 출력되는 access를 authorize에 입력")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            loginService.generateAndAttachTokens(response, userDetails);

            return ResponseEntity.ok("로그인 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }
}
