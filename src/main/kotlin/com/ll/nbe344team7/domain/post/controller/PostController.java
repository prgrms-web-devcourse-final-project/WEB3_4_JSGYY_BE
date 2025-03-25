package com.ll.nbe344team7.domain.post.controller;

import com.ll.nbe344team7.domain.post.dto.AuctionRequest;
import com.ll.nbe344team7.domain.post.dto.PostRequest;
import com.ll.nbe344team7.domain.post.dto.ReportDTO;
import com.ll.nbe344team7.domain.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @param loggedInMemberId
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-24
     */
    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestBody PostRequest request,
            @RequestHeader(value = "memberId") Long loggedInMemberId)
    {
        if (request.getTitle().trim().isEmpty()){
            return ResponseEntity.status(400).body(Map.of("message", "제목을 입력해주세요."));
        }

        if (request.getContent().trim().isEmpty()){
            return ResponseEntity.status(400).body(Map.of("message", "내용을 입력해주세요."));
        }

        if (request.getPrice() <= 0) {
            return ResponseEntity.status(400).body(Map.of("message", "가격을 0원 이상 입력해주세요."));
        }

        return ResponseEntity.ok(postService.createPost(request));
    }

    /**
     *
     * 게시글 삭제
     *
     * @param postId
     * @param loggedInMemberId
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-24
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long postId,
            @RequestHeader(value = "memberId") Long loggedInMemberId)
    {
        Long authorId = 1L;

        if (postId == 10000) {
            return ResponseEntity.status(404).body(Map.of("message", "해당 게시글이 존재하지 않습니다."));
        }

        if (!loggedInMemberId.equals(authorId)) {
            return ResponseEntity.status(403).body(Map.of("message", "해당 게시글의 삭제 권한이 없습니다."));
        }

        return ResponseEntity.ok(postService.deletePost(postId));
    }

    /**
     *
     * 게시글 수정
     *
     * @param postId
     * @param request
     * @param loggedInMemberId
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-24
     */
    @PutMapping("/{postId}")
    public ResponseEntity<?> modifyPost(
            @PathVariable Long postId,
            @RequestBody PostRequest request,
            @RequestHeader(value = "memberId") Long loggedInMemberId)
    {
        Long authorId = 1L;

        if (postId == 10000) {
            return ResponseEntity.status(404).body(Map.of("message", "해당 게시글이 존재하지 않습니다."));
        }

        if (!loggedInMemberId.equals(authorId)) {
            return ResponseEntity.status(403).body(Map.of("message", "해당 게시글의 수정 권한이 없습니다."));
        }

        return ResponseEntity.ok(postService.modifyPost(postId, request));
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
    public ResponseEntity<?> getPosts() {
        return ResponseEntity.ok(postService.getPosts());
    }

    /**
     *
     * 게시글 상세 조회
     *
     * @param postId
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-24
     */
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId) {

        if (postId == 10000) {
            return ResponseEntity.status(404).body(Map.of("message", "해당 게시글이 존재하지 않습니다."));
        }

        return ResponseEntity.ok(postService.getPost(postId));
    }

    @PostMapping("/{postId}/auction")
    public ResponseEntity<?> changeToAuction(
            @RequestBody AuctionRequest request,
            @RequestHeader(value = "memberId") Long loggedInMemberId,
            @PathVariable Long postId)
    {
        Long authorId = 1L;

        if (postId == 10000) {
            return ResponseEntity.status(404).body(Map.of("message", "해당 게시글이 존재하지 않습니다."));
        }

        if (!loggedInMemberId.equals(authorId)) {
            return ResponseEntity.status(403).body(Map.of("message", "해당 게시글의 경매 전환 권한이 없습니다."));
        }

        if (request.getStartedAt() == null) {
            return ResponseEntity.status(400).body(Map.of("message", "경매 시작일을 입력해주세요."));
        }

        if (request.getClosedAt() == null) {
            return ResponseEntity.status(400).body(Map.of("message", "경매 종료일을 입력해주세요."));
        }

        if (request.getStartPrice() <= 0) {
            return ResponseEntity.status(400).body(Map.of("message", "경매 시작가를 0원 이상 입력해주세요."));
        }

        return ResponseEntity.ok(postService.changeToAuction(postId, request));
    }

    /**
     *
     * 게시글 좋아요
     *
     * @param postId
     * @param loggedInMemberId
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    @GetMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId, @RequestHeader(value = "memberId") Long loggedInMemberId){
        if(postId == 10000){
            return ResponseEntity.status(404).body(Map.of("message", "해당 게시물을 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(Map.of("message", "좋아요 성공"));
    }

    /**
     *
     * 게시글 좋아요 취소
     *
     * @param postId
     * @param loggedInMemberId
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    @GetMapping("/{postId}/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable Long postId, @RequestHeader(value = "memberId") Long loggedInMemberId){
        if(postId == 10000){
            return ResponseEntity.status(404).body(Map.of("message", "해당 게시물을 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(Map.of("message", "좋아요 취소 성공"));
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
