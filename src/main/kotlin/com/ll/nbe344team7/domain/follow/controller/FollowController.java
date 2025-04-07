package com.ll.nbe344team7.domain.follow.controller;

import com.ll.nbe344team7.domain.follow.dto.FollowRequestDto;
import com.ll.nbe344team7.domain.follow.service.FollowService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
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

    @PostMapping
    public ResponseEntity<?> createFollow(@RequestBody FollowRequestDto requestDto,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(followService.createFollow(userDetails.getMemberId(), requestDto.getFollowingId()));
    }

    @DeleteMapping
    public ResponseEntity<?> unFollow(@RequestBody FollowRequestDto requestDto,
                                      @AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(followService.unFollow(userDetails.getMemberId(),requestDto.getFollowingId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFollows(@PathVariable Long id){
        if(id==10000L){
            return ResponseEntity.status(404).body(Map.of("message","팔로잉 중인 유저를 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(this.followService.listFollows(id));
    }
}
