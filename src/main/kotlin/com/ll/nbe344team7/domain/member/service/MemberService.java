package com.ll.nbe344team7.domain.member.service;


import com.ll.nbe344team7.domain.alarm.entity.Alarm;
import com.ll.nbe344team7.domain.member.dto.MemberDTO;
import com.ll.nbe344team7.domain.member.dto.OneDataDTO;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.exception.MemberException;
import com.ll.nbe344team7.domain.member.exception.MemberExceptionCode;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.domain.post.dto.response.PostListDto;
import com.ll.nbe344team7.domain.post.entity.Post;
import com.ll.nbe344team7.domain.post.repository.PostLikeRepository;
import com.ll.nbe344team7.domain.post.repository.PostRepository;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import com.ll.nbe344team7.global.redis.RedisRepository;
import com.ll.nbe344team7.global.security.exception.SecurityExceptionCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


/**
 * member service
 *
 * @author 이광석
 * @since 25.03.25
 */
@Service
public class MemberService {
    final private MemberRepository memberRepository;
    final private BCryptPasswordEncoder bCryptPasswordEncoder;
    final private PostRepository postRepository;
    final private PostLikeRepository postLikeRepository;
    final private RedisRepository redisRepository;
    public MemberService(MemberRepository memberRepository,
                         BCryptPasswordEncoder bCryptPasswordEncoder,
                         PostRepository postRepository,
                         PostLikeRepository postLikeRepository,
                         RedisRepository redisRepository
    ) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.redisRepository = redisRepository;
    }


    /**
     * 회원가입 메소드
     * password 인코딩
     * 기존에 가입한 회원인지 확인
     * @param memberDTO
     * @author 이광석
     * @since 2025-03-25
     */
    public void register(MemberDTO memberDTO) {
        String encodingPassword = bCryptPasswordEncoder.encode(memberDTO.getPassword());

        Member member = new Member(
                null,
                memberDTO.getUsername(),
                memberDTO.getName(),
                encodingPassword,
                memberDTO.getNickname(),
                memberDTO.getEmail(),
                memberDTO.getPhoneNum(),
                false,
                "ROLE_ADMIN",
                memberDTO.getAddress(),
                new ArrayList<>()         );

        try {
            memberRepository.save(member);
        }catch (DataIntegrityViolationException e){
            throw new RuntimeException("이미 존재하는 사용자 정보입니다");
        }
    }

    /**
     * 사용자 상세 정보 반환 메서드
     * 비밀번호는 반환하지 않도록 했다.
     * @param memberId
     * @return
     * @author 이광석
     * @since 2025-03-26
     */
    public MemberDTO myDetails(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

        return new MemberDTO(member.getId(),
                member.getName(),
                member.getUsername(),

                "","",
                member.getNickname(),
                member.getEmail(),
                member.getPhoneNum(),

                member.getRole(),
                member.getAddress());
    }

    /**
     * member 데이터 수정 메소드
     * @param category - OneData = 수정한 데이터
     * @param data
     * @param memberId
     * @author 이광석
     * @since 2025-04-01
     */
    @Transactional
    public void modifyMyDetails(String category, OneDataDTO data, Long memberId) {
        Member member = findMember(memberId);

        switch(category){
            case "phoneNum":
                member.setPhoneNum(data.getData());
                break;
            case "nickname" :
                member.setNickname(data.getData());
                break;
            case "address" :
                member.setAddress(data.getData());
                break;
        }
    }

    public Member getMember(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(()->new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
    }
    /**
     * 회원 탈퇴 메소드
     *
     * @param memberId
     * @author 이광석
     * @since 2025-04-01
     */
    @Transactional
    public void withdrawal(Long memberId, HttpServletRequest request, HttpServletResponse response) {

        //1. db에서 데이터 삭제
        Member member = findMember(memberId);
        memberRepository.delete(member);

        //2.redis 에서 데이터 삭제
        Cookie [] cookies = request.getCookies();

        if(cookies==null){
            throw new SecurityException(SecurityExceptionCode.NOT_FOUND_REFRESHTOKEN.getMessage());
        }

        String refreshToken = null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                refreshToken = cookie.getValue();
            }
        }

        if(refreshToken==null){
            throw new SecurityException(SecurityExceptionCode.NOT_FOUND_REFRESHTOKEN.getMessage());
        }
        redisRepository.delete(refreshToken);

        //3. 쿠키 삭제
        String refreshCookie = ResponseCookie
                .from("refresh",null)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("None")
                .maxAge(0)
                .build().toString();

        response.addHeader("Set-Cookie",refreshCookie);

    }


    /**
     * 회원 조회 메소드
     *
     * @param id
     * @return Member
     * @author 이광석
     * @since 2025-04-01
     */
    private Member findMember(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

    }

    public MemberDTO findMemberDTOByNickname(String nickname) {
        Member member = this.memberRepository.findByNickname(nickname);
        if(member == null){
            throw new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER);
        }
        return new MemberDTO(member);
    }

    /**
     *
     * 내 게시글 조회
     *
     * @param loginMemberId
     * @param pageable
     * @return
     *
     * @author GAEUN220
     * @since 2025-04-09
     */
    public Page<PostListDto> getMyPosts( Long loginMemberId, Pageable pageable) {
        if (!loginMemberId.equals(loginMemberId)) {
            throw new MemberException(MemberExceptionCode.NOT_AUTHORIZED);
        }

        Page<Post> posts = postRepository.findByMemberId(loginMemberId, pageable);

        return posts.map(post -> PostListDto.Companion.from(post));
    }

    /**
     *
     * 내가 좋아요한 게시글 조회
     *
     * @param loginMemberId
     * @param pageable
     * @return
     *
     * @author GAEUN220
     * @since 2025-04-09
     */
    public Page<PostListDto> getMyLikes(Long loginMemberId, Pageable pageable) {
        if (!loginMemberId.equals(loginMemberId)) {
            throw new MemberException(MemberExceptionCode.NOT_AUTHORIZED);
        }

        Page<Post> likedPosts = postLikeRepository.findLikedPostsByMemberId(loginMemberId, pageable);

        return likedPosts.map(post -> PostListDto.Companion.from(post));
    }
}
