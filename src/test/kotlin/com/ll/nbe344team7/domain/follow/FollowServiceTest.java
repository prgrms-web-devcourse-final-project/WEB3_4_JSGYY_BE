package com.ll.nbe344team7.domain.follow;

import com.ll.nbe344team7.domain.follow.dto.FollowListResponseDto;
import com.ll.nbe344team7.domain.follow.dto.FollowResponseDto;
import com.ll.nbe344team7.domain.follow.entity.Follow;
import com.ll.nbe344team7.domain.follow.repository.FollowRepository;
import com.ll.nbe344team7.domain.follow.service.FollowService;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.global.exception.FollowException;
import com.ll.nbe344team7.global.exception.GlobalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FollowServiceTest {

    @Autowired
    private FollowService followService;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("팔로우 성공")
    public void t1(){
        Optional<Member> user = memberRepository.findById(1L);
        Optional<Member> following = memberRepository.findById(1L);
        Long userId = user.get().getId();
        Long followingId = following.get().getId();

        FollowResponseDto responseDto = followService.createFollow(userId, followingId);

        assertNotNull(responseDto);
        assertEquals("팔로우 성공", responseDto.getMessage());
    }

    @Test
    @DisplayName("팔로우 성공 실패 - 맴버 조회 오류")
    public void t2(){
        Optional<Member> user = memberRepository.findById(1L);
        Long userId = user.get().getId();
        Long followingId = 999L;

        assertThrows(GlobalException.class,() -> {
            followService.createFollow(userId, followingId);
        });
    }

    @Test
    @DisplayName("팔로우 성공 실패 - 이미 팔로우된 유저")
    public void t3(){
        Optional<Member> user = memberRepository.findById(1L);
        Optional<Member> following = memberRepository.findById(2L);
        Long userId = user.get().getId();
        Long followingId = following.get().getId();

        assertThrows(FollowException.class,() -> {
            followService.createFollow(userId, followingId);
        });
    }

    @Test
    @DisplayName("언팔로우 성공")
    public void t4(){
        Optional<Member> user = memberRepository.findById(1L);
        Optional<Member> following = memberRepository.findById(2L);
        Long userId = user.get().getId();
        Long followingId = following.get().getId();

        Follow follow = followRepository.findByUserIdAndFollowingId(userId, followingId);

        FollowResponseDto responseDto = followService.unFollow(follow.getUserId(), follow.getFollowingId());

        assertEquals("언팔로우 성공", responseDto.getMessage());
    }

    @Test
    @DisplayName("언팔로우 실패 - 맴버 조회 실패")
    public void t5(){
        Optional<Member> user = memberRepository.findById(1L);
        Long userId = user.get().getId();
        Long followingId = 999L;

        assertThrows(GlobalException.class,() -> {
            followService.unFollow(userId, followingId);
        });
    }

    @Test
    @DisplayName("언팔로우 실패 - 팔로우하지 않은 유저 실패")
    public void t6(){
        Optional<Member> user = memberRepository.findById(1L);
        Optional<Member> following = memberRepository.findById(4L);
        Long userId = user.get().getId();
        Long followingId = following.get().getId();


        assertThrows(FollowException.class,() -> {
            followService.unFollow(userId, followingId);
        });
    }

    @Test
    @DisplayName("팔로잉 목록 조회 성공")
    public void t7(){
        Optional<Member> member = memberRepository.findById(1L);
        Pageable pageable = PageRequest.of(0, 5);
        Page<FollowListResponseDto> followList = followService.listFollows(member.get().getId(),pageable);

        assertNotNull(followList);
    }

    @Test
    @DisplayName("팔로잉 목록 조회 실패 - 목록이 존재하지 않는 경우")
    public void t8(){
        Optional<Member> member = memberRepository.findById(3L);
        Pageable pageable = PageRequest.of(0, 5);

        assertThrows(FollowException.class,() -> {
            followService.listFollows(member.get().getId(),pageable);
        });
    }
}