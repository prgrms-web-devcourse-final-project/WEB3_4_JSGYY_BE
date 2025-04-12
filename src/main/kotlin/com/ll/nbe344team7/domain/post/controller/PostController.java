package com.ll.nbe344team7.domain.post.controller;

import com.ll.nbe344team7.domain.post.dto.request.PostRequest;
import com.ll.nbe344team7.domain.post.dto.request.PostSearchRequest;
import com.ll.nbe344team7.domain.post.dto.request.ReportRequest;
import com.ll.nbe344team7.domain.post.dto.response.PostListDto;
import com.ll.nbe344team7.domain.post.service.PostService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "게시글 작성 요청 DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostRequest.class),
                            examples = @ExampleObject(
                                    description = "auctionStatus가 false일 경우 acutionRequest의 모든값은 null로 설정",
                                    value = """
                                            {
                                              "title": "바지",
                                              "content": "청바지입니다",
                                              "price": 20000,
                                              "category": "남성의류",
                                              "place": "서울시 관악구",
                                              "saleStatus": true,
                                              "auctionStatus": true,
                                              "auctionRequest": {
                                                "startedAt": "2025-03-27T10:00:00",
                                                "closedAt": "2025-04-10T10:00:00"
                                              }
                                            }
                                            """
                            )
                    )
            ),
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
                    @Parameter(name = "postId", description = "게시글 ID", required = true, example = "1")
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
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "수정할 게시글 내용",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "title": "바지",
                                              "content": "면바지",
                                              "price": 20000,
                                              "category": "남성의류",
                                              "place": "서울시 관악구",
                                              "saleStatus": true,
                                              "auctionStatus": true,
                                              "auctionRequest": {
                                                "startedAt": "2025-04-13T12:00:00",
                                                "endedAt": "2025-04-13T15:00:00"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            parameters = {
                    @Parameter(name = "postId", description = "게시글 ID", required = true, example = "1")
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
                    @Parameter(name = "page", description = "페이지 번호", required = true, schema = @Schema(type = "integer", defaultValue = "0")),
                    @Parameter(name = "size", description = "페이지 크기", required = true, schema = @Schema(type = "integer", defaultValue = "15")),
                    @Parameter(name = "sort", description = "정렬 기준", required = true, schema = @Schema(type = "string", defaultValue = "createdAt"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "게시글 목록 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PostListDto.class)),
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
                                                          "thumbnail": "qweqweqwe",
                                                          "createdAt": "2025-04-11T15:33:44.618817"
                                                        },
                                                        {
                                                          "id": 2,
                                                          "title": "aa12a",
                                                          "place": "가산",
                                                          "price": 2000,
                                                          "saleStatus": true,
                                                          "auctionStatus": true,
                                                          "thumbnail": null,
                                                          "createdAt": "2025-04-11T15:34:28.531806"
                                                        },
                                                        {
                                                          "id": 3,
                                                          "title": "바지",
                                                          "place": "서울시 관악구",
                                                          "price": 20000,
                                                          "saleStatus": true,
                                                          "auctionStatus": false,
                                                          "thumbnail": null,
                                                          "createdAt": "2025-04-12T14:40:29.88151"
                                                        },
                                                        {
                                                          "id": 4,
                                                          "title": "바지",
                                                          "place": "서울시 관악구",
                                                          "price": 30000,
                                                          "saleStatus": true,
                                                          "auctionStatus": false,
                                                          "thumbnail": null,
                                                          "createdAt": "2025-04-12T14:41:11.746954"
                                                        }
                                                      ],
                                                      "pageable": {
                                                        "pageNumber": 0,
                                                        "pageSize": 15,
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
                                                      "size": 15,
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
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 파라미터"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<?> getPosts(
            @Parameter(hidden = true)
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
                    @Parameter(name = "postId", description = "게시글 ID", required = true, example = "1")
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
                    @Parameter(name = "postId", description = "게시글 ID", required = true, example = "1")
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
                    @Parameter(name = "postId", description = "게시글 ID", required = true, example = "1")
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
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(

            ),
            parameters = {
                    @Parameter(name = "postId", description = "게시글 ID", required = true, example = "1")
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
