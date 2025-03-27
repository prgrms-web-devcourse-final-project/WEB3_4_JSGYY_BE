package com.ll.nbe344team7.global.security.service;

import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.global.security.dto.CustomUserData;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * UserDetailsService 구현체
 *
 * Db에서 사용자 정보를 가져와 CustomUserData에 저장
 *
 * @since 2025-03-26
 * @author 이광석
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    /**
     * Db에서 사용자 정보를 가져와 CustomUserDetails에 저장하는 메소드
     * @param username
     * @return
     * @throws UsernameNotFoundException
     *
     * @since 2025-03-26
     * @author 이광석
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findByUsername(username);

        if(memberEntity != null){
            CustomUserData customUserData = new CustomUserData(memberEntity.getId(),memberEntity.getUsername(),memberEntity.getRole(),memberEntity.getPassword());
            return new CustomUserDetails(customUserData);
        }
        return null;
    }
}
