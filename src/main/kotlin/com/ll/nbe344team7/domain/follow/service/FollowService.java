package com.ll.nbe344team7.domain.follow.service;

import com.ll.nbe344team7.domain.follow.dto.FollowListResponseDto;
import com.ll.nbe344team7.domain.follow.dto.FollowResponseDto;
import com.ll.nbe344team7.domain.follow.entity.Follow;
import com.ll.nbe344team7.domain.follow.repository.FollowRepository;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.global.exception.FollowException;
import com.ll.nbe344team7.global.exception.FollowExceptionCode;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    /**
     * 팔로우
     *
     * @param userId
     * @param followingId
     * @return
     *
     * @author kjm72
     * @since 2025-04-07
     */
    @Transactional
    public FollowResponseDto createFollow(Long userId, Long followingId) {
        Member user = memberRepository.findById(userId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
        Member following = memberRepository.findById(followingId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

        if (followRepository.existsByUserIdAndFollowingId(user.getId(), following.getId())) {
            throw new FollowException(FollowExceptionCode.ALREADY_FOLLOW);
        }

        Follow follow = new Follow(user.getId(), following.getId());
        followRepository.save(follow);
        return new FollowResponseDto("팔로우 성공");
    }

    /**
     * 언팔로우
     *
     * @param userId
     * @param followingId
     * @return
     *
     * @author kjm72
     * @since 2025-04-07
     */
    @Transactional
    public FollowResponseDto unFollow(Long userId, Long followingId) {
        Member user = memberRepository.findById(userId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
        Member following = memberRepository.findById(followingId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

        if (!followRepository.existsByUserIdAndFollowingId(user.getId(), following.getId())) {
            throw new FollowException(FollowExceptionCode.NOT_EXIST_FOLLOW);
        }
        Follow follow = followRepository.findByUserIdAndFollowingId(user.getId(), following.getId());
        followRepository.delete(follow);
        return new FollowResponseDto("언팔로우 성공");
    }

    /**
     * 팔로잉 목록 조회
     *
     * @param id
     * @param pageable
     * @return
     *
     * @author kjm72
     * @since 2025-04-07
     */
    @Transactional(readOnly = true)
    public Page<FollowListResponseDto> listFollows(Long id, Pageable pageable) {
        Page<Follow> followPage = followRepository.findAllByUserId(id, pageable);
        if (followPage.isEmpty()){
            throw new FollowException(FollowExceptionCode.NOT_EXIST_LIST);
        }
        List<Long> followingIds = followPage.stream().map(Follow::getFollowingId).toList();

        List<Member> members = memberRepository.findAllByIdIn(followingIds);

        Map<Long, Member> memberMap = members.stream().collect(Collectors.toMap(Member::getId, Function.identity()));
        return followPage.map(follow ->{
                    Member following = memberMap.get(follow.getFollowingId());
                    return new FollowListResponseDto(following);
                });
    }
}
