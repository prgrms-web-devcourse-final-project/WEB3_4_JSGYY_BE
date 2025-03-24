package com.ll.nbe344team7.domain.follow.controller;

import com.ll.nbe344team7.domain.follow.dto.FollowRequestDto;
import com.ll.nbe344team7.domain.follow.service.FollowService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createFollow(@RequestBody FollowRequestDto requestDto){
        if(requestDto.getFollowingId() == 10000L){
            return ResponseEntity.status(404).body(Map.of("message","해당 유저가 존재하지 않습니다."));
        } else if (requestDto.getFollowingId() == 10001L) {
            return ResponseEntity.status(404).body(Map.of("message","이미 팔로우한 유저입니다."));
        }
        return ResponseEntity.ok(this.followService.createFollow(requestDto.getUserId(), requestDto.getFollowingId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> unFollow(@PathVariable Long id){
        if(id==10000L){
            return ResponseEntity.status(404).body(Map.of("message","해당 유저가 존재하지 않습니다."));
        }
        return ResponseEntity.ok(this.followService.unFollow(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFollows(@PathVariable Long id){
        if(id==10000L){
            return ResponseEntity.status(404).body(Map.of("message","팔로잉 중인 유저를 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(this.followService.listFollows(id));
    }
}
