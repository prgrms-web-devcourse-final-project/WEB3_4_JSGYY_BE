package com.ll.nbe344team7.domain.post.controller;

import com.ll.nbe344team7.domain.post.dto.request.PostRequest;
import com.ll.nbe344team7.domain.post.dto.request.PostSearchRequest;
import com.ll.nbe344team7.domain.post.dto.request.ReportRequest;
import com.ll.nbe344team7.domain.post.dto.response.PostListDto;
import com.ll.nbe344team7.domain.post.service.PostService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "게시글 API")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     *
     * 게시글 작성
     *
     * @param request
     * @param userDetails
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-24
     */
    @Operation(
            summary = "게시글 작성",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 작성 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "1번 게시글이 작성되었습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "제목 입력 안 했을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "제목을 입력해주세요."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "내용 입력 안 했을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "내용을 입력해주세요."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "가격이 0원 이하일 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "가격을 0원 이상 입력해주세요."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "장소 입력 안 했을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "장소를 입력해주세요."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "카테고리 입력 안 했을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "카테고리를 선택해주세요."
                                            }
                                            """
                            )
                    ))
            }
    )
    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestBody PostRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        return ResponseEntity.ok(postService.createPost(request, userDetails.getMemberId()));
    }

    /**
     *
     * 게시글 삭제
     *
     * @param postId
     * @param userDetails
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-24
     */
    @Operation(
            summary = "게시글 삭제",
            parameters = {
                    @Parameter(name = "postId", description = "게시글 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 삭제 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "1번 게시글이 삭제되었습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "존재하지 않는 게시글입니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "401", description = "게시글 작성자가 아닐 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "게시글 권한이 없습니다."
                                            }
                                            """
                            )
                    ))
            }

    )
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        return ResponseEntity.ok(postService.deletePost(postId, userDetails.getMemberId()));
    }

    /**
     *
     * 게시글 수정
     *
     * @param postId
     * @param request
     * @param userDetails
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-24
     */
    @Operation(
            summary = "게시글 수정",
            parameters = {
                    @Parameter(name = "postId", description = "게시글 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 수정 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "1번 게시글이 수정되었습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "존재하지 않는 게시글입니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "401", description = "게시글 작성자가 아닐 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "게시글 권한이 없습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "제목 입력 안 했을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "제목을 입력해주세요."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "내용 입력 안 했을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "내용을 입력해주세요."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "가격이 0원 이하일 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "가격을 0원 이상 입력해주세요."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "장소 입력 안 했을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "장소를 입력해주세요."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "카테고리 입력 안 했을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "카테고리를 선택해주세요."
                                            }
                                            """
                            )
                    ))
            }
    )
    @PutMapping("/{postId}")
    public ResponseEntity<?> modifyPost(
            @PathVariable Long postId,
            @RequestBody PostRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        postService.modifyPost(postId, request, userDetails.getMemberId());

        if (request.getAuctionRequest() != null) {
            postService.changeToAuction(postId, request.getAuctionRequest(), userDetails.getMemberId());
        }

        return ResponseEntity.ok(Map.of("message", postId + "번 게시글 수정 성공"));
    }

    /**
     *
     * 게시글 목록 조회
     *
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-24
     */
    @Operation(
            summary = "게시글 목록 조회",
            parameters = {
                    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", required = false),
                    @Parameter(name = "size", description = "페이지 크기 (default = 15)", required = false),
                    @Parameter(name = "sort", description = "정렬 기준 (예: createdAt,desc)", required = false),
                    @Parameter(name = "category", description = "카테고리", required = false),
                    @Parameter(name = "minPrice", description = "최소가격", required = false),
                    @Parameter(name = "maxPrice", description = "최대가격", required = false),
                    @Parameter(name = "saleStatus", description = "판매상태", required = false),
                    @Parameter(name = "keyword", description = "검색어", required = false),
                    @Parameter(name = "place", description = "위치", required = false)
            }
    )
    @GetMapping
    public ResponseEntity<?> getPosts(
            @PageableDefault(size = 15,
                    page = 0,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC)
            Pageable pageable,
            @ModelAttribute PostSearchRequest searchRequest)
    {
        Page<PostListDto> postList = postService.getPostsBySearch(pageable, searchRequest);

        return ResponseEntity.ok(postList);
    }

    /**
     *
     * 게시글 상세 조회
     *
     * @param postId
     * @param userDetails
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-24
     */
    @Operation(
            summary = "게시글 상세 조회",
            parameters = {
                    @Parameter(name = "postId", description = "게시글 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 상세 조회 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "title": "제목",
                                                "content": "내용",
                                                "price": 10000,
                                                ”category”: “남성의류”,
                                                "auctionStatus": true,
                                                "auctionRequest": {
                                                    "startedAt": "2025-03-27T10:00:00",
                                                    "closedAt": "2025-04-10T10:00:00"
                                                }
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "존재하지 않는 게시글입니다."
                                            }
                                            """
                            )
                    ))
            }
    )
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        return ResponseEntity.ok(postService.getPost(postId, userDetails.getMemberId()));
    }

    /**
     *
     * 게시글 좋아요
     *
     * @param postId
     * @param userDetails
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-31
     */
    @Operation(
            summary = "게시글 좋아요",
            parameters = {
                    @Parameter(name = "postId", description = "게시글 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 좋아요 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "1번 게시글 좋아요 성공"
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "존재하지 않는 게시글입니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "이미 좋아요를 눌렀을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "이미 좋아요를 눌렀습니다."
                                            }
                                            """
                            )
                    ))
            }
    )
    @GetMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId,
                                      @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        return ResponseEntity.ok(postService.likePost(postId, userDetails.getMemberId()));
    }

    /**
     *
     * 게시글 좋아요 취소
     *
     * @param postId
     * @param userDetails
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-31
     */
    @Operation(
            summary = "좋아요 취소",
            parameters = {
                    @Parameter(name = "postId", description = "게시글 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 좋아요 취소 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "1번 게시글 좋아요 취소 성공"
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "존재하지 않는 게시글입니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "좋아요를 누르지 않았을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "존재하지 않는 좋아요입니다."
                                            }
                                            """
                            )
                    ))
            }
    )
    @GetMapping("/{postId}/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable Long postId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        return ResponseEntity.ok(postService.unlikePost(postId, userDetails.getMemberId()));
    }

    /**
     *
     * 게시글 신고
     *
     * @param reportRequest
     * @param postId
     * @param userDetails
     * @return
     *
     * @author GAEUN220
     * @since 2025-04-03
     */
    @Operation(
            summary = "게시글 신고",
            parameters = {
                    @Parameter(name = "postId", description = "게시글 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 신고 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "1번 게시글이 신고되었습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "존재하지 않는 게시글입니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "신고 제목 입력하지 않았을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" :  “신고 제목은 최소 1자, 최대 30자로 입력해주세요.”
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "신고 내용 입력하지 않았을 때", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" :  “신고 내용은 최소 1자, 최대 100자로 입력해주세요.”
                                            }
                                            """
                            )
                    ))
            }
    )
    @PostMapping("/{postId}/reports")
    public ResponseEntity<?> reportPost(
            @RequestBody ReportRequest reportRequest,
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        return ResponseEntity.ok(postService.reportPost(reportRequest, postId, userDetails.getMemberId()));
    }
}
