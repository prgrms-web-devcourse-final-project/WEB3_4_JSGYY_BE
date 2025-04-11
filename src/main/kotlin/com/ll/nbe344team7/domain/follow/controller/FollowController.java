package com.ll.nbe344team7.domain.follow.controller;

import com.ll.nbe344team7.domain.follow.dto.FollowListResponseDto;
import com.ll.nbe344team7.domain.follow.dto.FollowRequestDto;
import com.ll.nbe344team7.domain.follow.dto.FollowResponseDto;
import com.ll.nbe344team7.domain.follow.service.FollowService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *
 *
 * @author kjm72
 * @since 2025-03-24
 */
@RestController
@RequestMapping("/api/follow")
@Tag(name = "팔로우 API")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    /**
     * 팔로우
     *
     * @param requestDto
     * @param userDetails
     * @return
     *
     * @author kjm72
     * @since 2025-04-07
     */
    @Operation(
            summary = "팔로우",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "팔로우 요청 DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FollowRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200",description = "팔로우 성공", content = @Content(mediaType = "application/json",schema = @Schema(implementation = FollowResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "맴버가 조회되지 않습니다.", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "407", description = "이미 팔로우한 유저입니다.", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ResponseEntity<?> createFollow(@RequestBody FollowRequestDto requestDto,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(followService.createFollow(userDetails.getMemberId(), requestDto.getFollowingId()));
    }

    /**
     * 언팔로우
     *
     * @param requestDto
     * @param userDetails
     * @return
     *
     * @author kjm72
     * @since 2025-04-07
     */
    @Operation(
            summary = "언팔로우",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "팔로우 요청 DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FollowRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200",description = "언팔로우 성공", content = @Content(mediaType = "application/json",schema = @Schema(implementation = FollowResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "맴버가 조회되지 않습니다.", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "407", description = "팔로우하지 않은 유저입니다.", content = @Content(mediaType = "application/json"))
            }
    )
    @DeleteMapping
    public ResponseEntity<?> unFollow(@RequestBody FollowRequestDto requestDto,
                                      @AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(followService.unFollow(userDetails.getMemberId(),requestDto.getFollowingId()));
    }

    /**
     * 팔로잉 목록 조회
     *
     * @param userDetails
     * @param pageable
     * @return
     *
     * @author kjm72
     * @since 2025-04-07
     */
    @Operation(
            summary = "팔로잉 목록 조회",
            description = "sort의 값을 \"id\"로 변경하고 진행",
            parameters = {
                    @Parameter(name = "page", description = "페이지 번호", example = "0"),
                    @Parameter(name = "size", description = "페이지 크기", example = "5"),
                    @Parameter(name = "sort", description = "정렬 기준", example = "id")
            },
            responses = {
                    @ApiResponse(responseCode = "200",description = "팔로잉 목록 조회 성공",content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "following": {
                                                "content": [
                                                  {
                                                    "followingId": 5,
                                                    "nickname": "nickname5"
                                                  },
                                                  {
                                                    "followingId": 4,
                                                    "nickname": "nickname4"
                                                  }
                                                ],
                                                "pageable": {
                                                  "pageNumber": 0,
                                                  "pageSize": 5,
                                                  "sort": {
                                                    "empty": false,
                                                    "unsorted": false,
                                                    "sorted": true
                                                  },
                                                  "offset": 0,
                                                  "paged": true,
                                                  "unpaged": false
                                                },
                                                "last": true,
                                                "totalElements": 4,
                                                "totalPages": 1,
                                                "size": 5,
                                                "number": 0,
                                                "sort": {
                                                  "empty": false,
                                                  "unsorted": false,
                                                  "sorted": true
                                                },
                                                "first": true,
                                                "numberOfElements": 4,
                                                "empty": false
                                              }
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "팔로잉 중인 유저 목록이 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping
    public ResponseEntity<?> getFollows(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @Parameter(hidden = true) @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        Page<FollowListResponseDto> response = followService.listFollows(userDetails.getMemberId(),pageable);
        return ResponseEntity.ok(Map.of("following", response));
    }
}
