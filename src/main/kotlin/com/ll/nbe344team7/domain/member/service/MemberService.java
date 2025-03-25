package com.ll.nbe344team7.domain.member.service;


import com.ll.nbe344team7.domain.member.dto.MemberDTO;
import com.ll.nbe344team7.domain.member.entity.MemberEntity;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    final private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public void register(MemberDTO memberDTO){
        MemberEntity member =new MemberEntity(memberDTO);
        memberRepository.save(member);
    }

}
