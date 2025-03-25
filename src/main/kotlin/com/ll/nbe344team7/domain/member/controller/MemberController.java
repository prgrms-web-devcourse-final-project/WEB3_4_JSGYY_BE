package com.ll.nbe344team7.domain.member.controller;


import com.ll.nbe344team7.domain.member.MemberService;
import com.ll.nbe344team7.domain.member.dto.MemberDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("api")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/test")
    public void createTest(){
        memberService.create();
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String,String>> register(@RequestBody() MemberDTO memberDTO){
        Map<String,String> mockResponseMap = new HashMap<>();

        if(memberDTO.getEmail().isBlank() || memberDTO.getName().isBlank() || memberDTO.getNickName().isBlank()||
        memberDTO.getUsername().isBlank() || memberDTO.getPassword().isBlank()||memberDTO.getPassword2().isBlank()){

            mockResponseMap.put("message", "회원가입 실패: 필수 항목 누락");
            return ResponseEntity.badRequest().body(mockResponseMap);
        }


        mockResponseMap.put("Message","회원가입 성공");
        return ResponseEntity.ok(mockResponseMap);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody() MemberDTO memberDTO){
        Map<String,String> mockResponseMap = new HashMap<>();

        if(memberDTO.getPassword().isBlank() || memberDTO.getUsername().isBlank()){
            mockResponseMap.put("Message","이메일 혹은 비밀번호를 잘못 입력했습니다");
            return ResponseEntity.ok(mockResponseMap);

        }
        mockResponseMap.put("Message","로그인 성공");
        return ResponseEntity.ok(mockResponseMap);
    }

    @GetMapping("/auth/logout")
    public ResponseEntity<Map<String,String>> logout(){
        Map<String,String> mockResponseMap = new HashMap<>();
        mockResponseMap.put("Message","로그아웃 성공");
        return ResponseEntity.ok(mockResponseMap);
    }

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

    @DeleteMapping("/member/withdrawal")
    public ResponseEntity<Map<String,Object>> withdrawal(){
        Map<String,Object> mockResponseMap = new HashMap<>();
        mockResponseMap.put("Message","회원 탈퇴 성공");
        return ResponseEntity.ok(mockResponseMap);
    }
}
