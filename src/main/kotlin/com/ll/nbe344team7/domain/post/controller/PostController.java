package com.ll.nbe344team7.domain.post.controller;

import com.ll.nbe344team7.domain.post.dto.request.PostRequest;
import com.ll.nbe344team7.domain.post.dto.request.PostSearchRequest;
import com.ll.nbe344team7.domain.post.dto.response.PostListDto;
import com.ll.nbe344team7.domain.post.dto.response.ReportDTO;
import com.ll.nbe344team7.domain.post.service.PostService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/posts")
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
    @PostMapping
    public ResponseEntity<?> createPost(
            @Valid @RequestBody PostRequest request,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        return ResponseEntity.ok(postService.createPost(request, images, userDetails.getMemberId()));
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
    @PutMapping("/{postId}")
    public ResponseEntity<?> modifyPost(
            @PathVariable Long postId,
            @RequestBody PostRequest request,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        postService.modifyPost(postId, request, images, userDetails.getMemberId());

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
    @GetMapping
    public ResponseEntity<?> getPosts(
            @PageableDefault(size = 10,
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
     * @param reportDTO
     * @param postId
     * @param loggedInMemberId
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    @PostMapping("/{postId}/reports")
    public ResponseEntity<?> reportPost(@RequestBody ReportDTO reportDTO, @PathVariable Long postId, @RequestHeader(value = "memberId") Long loggedInMemberId){
        if(postId == 10000){
            return ResponseEntity.status(404).body(Map.of("message", "해당 게시물을 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(Map.of("message", "게시글 신고가 완료되었습니다."));
    }
}
