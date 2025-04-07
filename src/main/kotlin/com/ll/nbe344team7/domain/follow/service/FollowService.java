package com.ll.nbe344team7.domain.follow.service;

import com.ll.nbe344team7.domain.follow.dto.CreateFollowResponseDto;
import com.ll.nbe344team7.domain.follow.entity.Follow;
import com.ll.nbe344team7.domain.follow.repository.FollowRepository;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.global.exception.FollowException;
import com.ll.nbe344team7.global.exception.FollowExceptionCode;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
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

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    public FollowService(FollowRepository followRepository, MemberRepository memberRepository) {
        this.followRepository = followRepository;
        this.memberRepository = memberRepository;
    }

    public CreateFollowResponseDto createFollow(Long userId, Long followingId) {
        Member user = memberRepository.findById(userId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
        Member following = memberRepository.findById(followingId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

        if (followRepository.existsByUserAndFollowing(user, following)) {
            throw new FollowException(FollowExceptionCode.ALREADY_FOLLOW);
        }

        Follow follow = new Follow(user, following);
        followRepository.save(follow);
        return new CreateFollowResponseDto("팔로우 성공");
    }

    public Map<Object, Object> unFollow(Long userId, Long followingId) {
        Member user = memberRepository.findById(userId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
        Member following = memberRepository.findById(followingId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

        if (!followRepository.existsByUserAndFollowing(user, following)) {
            throw new FollowException(FollowExceptionCode.NOT_EXIST_FOLLOW);
        }
        Follow follow = followRepository.findByUserAndFollowing(user, following);
        followRepository.delete(follow);
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
