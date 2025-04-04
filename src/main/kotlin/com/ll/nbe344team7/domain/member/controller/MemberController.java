package com.ll.nbe344team7.domain.member.controller;


import com.ll.nbe344team7.domain.member.dto.MemberDTO;
import com.ll.nbe344team7.domain.member.dto.OneData;
import com.ll.nbe344team7.domain.member.service.MemberService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "맴버 API")
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
    @Operation(summary = "회원가입")
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
    @Operation(summary = "로그아웃")
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
    @Operation(summary = "내 상세 정보 조회")
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
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/member/withdrawal")
    public ResponseEntity<Map<String,Object>> withdrawal(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody OneData data
    ){
        memberService.withdrawal(data,userDetails.getMemberId());
        Map<String,Object> result = new HashMap<>();
        result.put("message","회원탈퇴 성공");

        return ResponseEntity.ok(result);

    }



    @Operation(summary = "내 정보 수정")
    @PutMapping("/member/modify/{category}")
    public ResponseEntity<Map<String,Object>> modifyMyDetails(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody OneData data,
            @PathVariable(value = "category") String category
    ){
        Map<String,Object> request = new HashMap<>();
        memberService.modifyMyDetails(category,data,customUserDetails.getMemberId());
        request.put("message","수정 성공");
        return ResponseEntity.ok(request);
    }

    @Operation(summary = "닉네임 회원 검색")
    @GetMapping("/member/nickname")
    public ResponseEntity<?> findMemberByNickname(@RequestParam("nickname") String nickname){
        return ResponseEntity.ok(this.memberService.findMemberDTOByNickname(nickname));
    }

}
