package com.ll.nbe344team7.domain.member.controller;


import com.ll.nbe344team7.domain.member.dto.MemberDTO;
import com.ll.nbe344team7.domain.member.service.MemberService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;



/**
 * member 컨트롤러
 *
 * @author 이광석
 * @since 25.03.25
 */
@RestController
@RequestMapping("api")
public class MemberController {


    private final MemberService memberService;

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    /**
     * 회원가입
     * @param memberDTO -username,name,password,nickName,email,phoneNum
     * @return ResponseEntity<String>
     *
     * @author 이광석
     * @since 25.03.25
     */
    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody() MemberDTO memberDTO){

        try {
            memberService.register(memberDTO);
            return ResponseEntity.ok("회원가입 성공");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("이미 존재하는 사용자 정보 입니다");
        }

    }



    /**
     * 로그아웃
     * @return ResponseEntity<Map<String,String>>
     * @author 이광석
     * @since 25.03.25
     */
    @GetMapping("/auth/logout")
    public ResponseEntity<Map<String,String>> logout(){
        Map<String,String> mockResponseMap = new HashMap<>();
        mockResponseMap.put("Message","로그아웃 성공");
        return ResponseEntity.ok(mockResponseMap);
    }

    /**
     * 내  상세 정보
     *
     * @return ResponseEntity<Map<String, Object>>
     * @author 이광석
     * @since 25.03.25
     */
    @GetMapping("/member/mydetails")
    public ResponseEntity<Map<String, Object>> myDetails(
        @AuthenticationPrincipal    CustomUserDetails customUserDetails
    ){
        MemberDTO memberDTOS= memberService.myDetails(customUserDetails.getMemberId());

        Map<String,Object> map = new HashMap<>();
        map.put("message","데이터 전달 성공");
        map.put("data",memberDTOS);

        return ResponseEntity.ok(map);
    }


    /**
     * 회원 탈퇴
     * @return  ResponseEntity<Map<String,Object>>
     * @author 이광석
     * @since 25.03.25
     */
    @DeleteMapping("/member/withdrawal")
    public ResponseEntity<Map<String,Object>> withdrawal(){
        Map<String,Object> mockResponseMap = new HashMap<>();
        mockResponseMap.put("Message","회원 탈퇴 성공");
        return ResponseEntity.ok(mockResponseMap);
    }

    /**
     * 로그인후 CustomUserDetails 를 테스트하기위한 메소드
     * @param customUserDetails
     * @return ResponseEntity<Map<String,Object>>
     * @author 이광석
     * @since 2025-03-26
     */
    @GetMapping("/")
    public ResponseEntity<Map<String,Object>> userDetailTest(
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Map<String,Object> test = new HashMap<>();
        test.put("이름",customUserDetails.getUsername());
        test.put("권한",customUserDetails.getRole());
        test.put("아이디", customUserDetails.getMemberId());

        return ResponseEntity.ok(test);

    }
}
