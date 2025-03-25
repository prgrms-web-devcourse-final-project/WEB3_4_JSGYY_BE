package com.ll.nbe344team7.domain.member.service;


import com.ll.nbe344team7.domain.member.dto.MemberDTO;
import com.ll.nbe344team7.domain.member.entity.MemberEntity;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import org.springframework.dao.DataIntegrityViolationException;
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
    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }



    public void register(MemberDTO memberDTO) {
        String encodingPassword = bCryptPasswordEncoder.encode(memberDTO.getPassword());

        MemberEntity memberEntity = new MemberEntity(
                null,
                memberDTO.getUsername(),
                memberDTO.getName(),
                encodingPassword,
                memberDTO.getNickName(),
                memberDTO.getEmail(),
                memberDTO.getPhone_num(),
                false,
                2
        );

        try {
            memberRepository.save(memberEntity);
        }catch (DataIntegrityViolationException e){
            throw new RuntimeException("이미 존재하는 사용자 정보입니다");
        }
    }

}
