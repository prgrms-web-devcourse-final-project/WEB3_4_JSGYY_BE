package com.ll.nbe344team7.domain.member.controller;


import com.ll.nbe344team7.domain.member.dto.MemberDTO;
import com.ll.nbe344team7.domain.member.dto.OneData;
import com.ll.nbe344team7.domain.member.service.MemberService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    @Operation(
            summary = "회원가입",
            description = "가입할 회원 정보를 RequestBody에 작성하고 실행하세요",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "이미 존재하는 사용자 젇보 입니다.", content = @Content(mediaType = "application/json"))
            }
    )
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
    @Operation(
            summary = "로그아웃",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "로그아웃 성공"
                                            }
                                            """)
                    ))
            }
    )
    @PostMapping("/auth/logout")
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
    @Operation(
            summary = "내 상세 정보 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "내 정보 조회 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "data": {
                                                "id": 1,
                                                "name": "tmp",
                                                "username": "admin1",
                                                "password": "",
                                                "password2": "",
                                                "nickname": "nickname1",
                                                "email": "email",
                                                "phoneNum": "phoneNum",
                                                "role": "ROLE_ADMIN",
                                                "address": ""
                                              },
                                              "message": "데이터 전달 성공"
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "맴버가 조회되지 않습니다.", content = @Content(mediaType = "application/json"))
            }
    )
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
    @Operation(
            summary = "회원 탈퇴",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json"),
                    required = true,
                    description = "탈퇴를 위한 비밀번호를 작성해주세요 ex) \"data\" : \"12345\""

            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원탈퇴 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "회원탈퇴 성공"
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "회원탈퇴 실패(비밀번호 불일치)", content = @Content(mediaType = "application/json"))
            }

    )
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



    @Operation(
            summary = "내 정보 수정",
            description = """
                    카테고리(nickname, phoneNum, address)에 따라 유저의 정보를 수정합니다.
                    parameter에 수정할 항목을 작성하고 requestbody에 항목의 수정할 내용을 작성해주세요.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json"),
                    required = true,
                    description = """
                            수정할 항목의 내용을 작성해주세요
                            nickname => 수정할 닉네임 ex) "data" : "newNickname"
                            phoneNum => 수정할 전화번호 ex) "data" : "newPhoneNum"
                            address => 수정할 주소 ex) "data" : "newAddress"
                            """
            ),
            parameters = {
                @Parameter(name = "category", description = "수정할 항목 (nickname, phoneNum, address)", required = true, example = "nickname")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "수정 성공"
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "맴버가 조회되지 않습니다.", content = @Content(mediaType = "application/json"))
            }
    )
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


    @Operation(
            summary = "닉네임 회원 검색",
            parameters = {
                    @Parameter(name = "nickname", description = "검색할 닉네임", required = true, example = "nickname1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "닉네임으로 회원 조회 성공", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDTO.class)
                    )),
                    @ApiResponse(responseCode = "404", description = "맴버가 조회되지 않습니다.", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/member/nickname")
    public ResponseEntity<?> findMemberByNickname(@RequestParam("nickname") String nickname){
        return ResponseEntity.ok(this.memberService.findMemberDTOByNickname(nickname));
    }

    /**
     *
     * 내 게시글 조회
     *
     * @param customUserDetails
     * @param pageable
     * @return
     *
     * @author GAEUN220
     * @since 2025-04-09
     */
    @Operation(
            summary = "내 게시글 조회",
            parameters = {
                    @Parameter(name = "page", description = "페이지 번호", required = true, example = "0"),
                    @Parameter(name = "size", description = "페이지 크기", required = true, example = "15"),
                    @Parameter(name = "sort", description = "정렬 기준", required = true ,example = "createdAt")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "content": [
                                                {
                                                  "id": 1,
                                                  "title": "aaa",
                                                  "place": "서울시 금천구",
                                                  "price": 2000,
                                                  "saleStatus": true,
                                                  "auctionStatus": false,
                                                  "thumbnail": null,
                                                  "createdAt": "2025-04-11T15:33:44.618817"
                                                },
                                                {
                                                  "id": 2,
                                                  "title": "aa12a",
                                                  "place": "서울시 금천구",
                                                  "price": 2000,
                                                  "saleStatus": true,
                                                  "auctionStatus": true,
                                                  "thumbnail": null,
                                                  "createdAt": "2025-04-11T15:34:28.531806"
                                                }
                                              ],
                                              "pageable": {
                                                "pageNumber": 0,
                                                "pageSize": 15,
                                                "sort": {
                                                  "empty": false,
                                                  "sorted": true,
                                                  "unsorted": false
                                                },
                                                "offset": 0,
                                                "paged": true,
                                                "unpaged": false
                                              },
                                              "last": true,
                                              "totalElements": 2,
                                              "totalPages": 1,
                                              "size": 15,
                                              "number": 0,
                                              "sort": {
                                                "empty": false,
                                                "sorted": true,
                                                "unsorted": false
                                              },
                                              "numberOfElements": 2,
                                              "first": true,
                                              "empty": false
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "401", description = "내 게시글만 조회할 수 있습니다.", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/member/myposts")
    public ResponseEntity<?> getMyPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(hidden = true) @PageableDefault(size = 15,
                    page = 0,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(memberService.getMyPosts(customUserDetails.getMemberId(), pageable));
    }

    /**
     *
     * 네가 좋아요한 게시글 조회
     *
     * @param customUserDetails
     * @param pageable
     * @return
     *
     * @author GAEUN220
     * @since 2025-04-09
     */
    @Operation(
            summary = "내가 좋아요한 게시글 조회",
            parameters = {
                    @Parameter(name = "page", description = "페이지 번호", required = true, example = "0"),
                    @Parameter(name = "size", description = "페이지 크기", required = true, example = "15"),
                    @Parameter(name = "sort", description = "정렬 기준", required = true ,example = "createdAt")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "content": [
                                                  {
                                                    "id": 1,
                                                    "title": "aaa",
                                                    "place": "가산",
                                                    "price": 2000,
                                                    "saleStatus": true,
                                                    "auctionStatus": false,
                                                    "thumbnail": null,
                                                    "createdAt": "2025-04-11T15:33:44.618817"
                                                  }
                                                ],
                                                "pageable": {
                                                  "pageNumber": 0,
                                                  "pageSize": 15,
                                                  "sort": {
                                                    "empty": false,
                                                    "sorted": true,
                                                    "unsorted": false
                                                  },
                                                  "offset": 0,
                                                  "paged": true,
                                                  "unpaged": false
                                                },
                                                "last": true,
                                                "totalElements": 1,
                                                "totalPages": 1,
                                                "size": 15,
                                                "number": 0,
                                                "sort": {
                                                  "empty": false,
                                                  "sorted": true,
                                                  "unsorted": false
                                                },
                                                "first": true,
                                                "numberOfElements": 1,
                                                "empty": false
                                              }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "401", description = "내 게시글만 조회할 수 있습니다.", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/member/mylikes")
    public ResponseEntity<?> getMyLikes(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(hidden = true) @PageableDefault(size = 15,
                    page = 0,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(memberService.getMyLikes(customUserDetails.getMemberId(), pageable));
    }
}
