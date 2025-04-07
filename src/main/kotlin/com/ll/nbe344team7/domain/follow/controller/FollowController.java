package com.ll.nbe344team7.domain.follow.controller;

import com.ll.nbe344team7.domain.follow.dto.FollowListResponseDto;
import com.ll.nbe344team7.domain.follow.dto.FollowRequestDto;
import com.ll.nbe344team7.domain.follow.service.FollowService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @GetMapping
    public ResponseEntity<?> getFollows(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        Pageable pageable) {
        Page<FollowListResponseDto> response = followService.listFollows(userDetails.getMemberId(),pageable);
        return ResponseEntity.ok(Map.of("following", response));
    }
}
