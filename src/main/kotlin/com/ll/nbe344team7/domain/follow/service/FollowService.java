package com.ll.nbe344team7.domain.follow.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author kjm72
 * @since 2025-03-24
 */
@Service
public class FollowService {

    public Map<Object, Object> createFollow(Long userId, Long followingId) {
        return Map.of("message","팔로우 성공");
    }

    public Map<Object, Object> unFollow(Long id) {
        return Map.of("message","언팔로우 성공");
    }

    public Object listFollows(Long id) {
        List<Map<String, Object>> list = List.of(
                Map.of("followingId",1,"nickname","시계매니아"),
                Map.of("followingId",2,"nickname","모아신발")

        );
        return Map.of("following",list);
    }
}
