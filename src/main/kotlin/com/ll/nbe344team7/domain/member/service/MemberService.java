package com.ll.nbe344team7.domain.member.service;


import com.ll.nbe344team7.domain.member.dto.MemberDTO;
import com.ll.nbe344team7.domain.member.dto.OneData;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


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
    public MemberService(MemberRepository memberRepository,
                         BCryptPasswordEncoder bCryptPasswordEncoder,
                         PostRepository postRepository,
                         PostLikeRepository postLikeRepository
    ) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
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
                memberDTO.getAddress()
        );

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
     * @param category
     * @param data
     * @param memberId
     * @author 이광석
     * @since 2025-04-01
     */
    public void modifyMyDetails(String category, OneData data, Long memberId) {
        Member preMember = findMember(memberId);
        MemberDTO preMemberDTO = new MemberDTO(preMember);

        switch(category){
            case "phoneNum":
                preMemberDTO.setPhoneNum(data.getData());
                break;
            case "nickname" :
                preMemberDTO.setNickname(data.getData());
                break;
            case "address" :
                preMemberDTO.setAddress(data.getData());
                break;
        }

        Member newMember = new Member(preMemberDTO);
        memberRepository.save(newMember);

    }

    public Member getMember(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(()->new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
    }
    /**
     * 회원 탈퇴 메소드
     *

     * @param data
     * @param memberId
     * @return boolean
     * @author 이광석
     * @since 2025-04-01
     */
    public boolean withdrawal(OneData data, Long memberId) {
        Member member = findMember(memberId);

        if(!member.getPassword().equals(bCryptPasswordEncoder.encode(data.getData()))){
            return false;
        }
        memberRepository.delete(member);
        return true;
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
