package com.ll.nbe344team7.domain.member.controller;


import com.ll.nbe344team7.domain.member.service.MemberService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.ll.nbe344team7.domain.member.dto.MemberDTO;


/**
 * member 컨트롤러
 *
 * @author 이광석
 * @since 25.03.25
 */
@RestController
@RequestMapping("api")
public class MemberController {

    final private MemberService memberService;

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

//    @PostMapping("/auth/login")
//    public ResponseEntity<Map<String,String>> login(@RequestBody() MemberDTO memberDTO){
//        Map<String,String> mockResponseMap = new HashMap<>();
//
//        if(memberDTO.getPassword().isBlank() || memberDTO.getUsername().isBlank()){
//            mockResponseMap.put("Message","이메일 혹은 비밀번호를 잘못 입력했습니다");
//            return ResponseEntity.ok(mockResponseMap);
//
//        }
//        mockResponseMap.put("Message","로그인 성공");
//        return ResponseEntity.ok(mockResponseMap);
//    }

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
    public ResponseEntity<Map<String, Object>> myDetails(){
        Map<String,Object> mockDataMap = new HashMap<>();
        mockDataMap.put("id" , 1);
        mockDataMap.put("name" , "홍길동");
        mockDataMap.put("nickname","임꺽정");
        mockDataMap.put("email","tmp@gmail.com");
        mockDataMap.put("phone_num","010-8080-8080");
        mockDataMap.put("create_at","2024-03-21T12:00:00");

        Map<String,Object> mockResponseMap= new HashMap<>();
        mockResponseMap.put("message","데이터 전달 성공");
        mockResponseMap.put("data",mockDataMap);

        return ResponseEntity.ok(mockResponseMap);
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
}
