package com.ll.nbe344team7.domain.member.service;


import com.ll.nbe344team7.domain.member.dto.MemberDTO;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;


    Member member;
    MemberDTO memberDTO;

    @BeforeEach
    public void setUp(){
        member = new Member(
                null,
                "testUsername",
                "testName",
                "testPassword",
                "testNickname",
                "test@email.com",
                "010-test-test",
                false,
                "ROLE_ADMIN",
                "주소"
        );

        memberDTO = new MemberDTO(
                null,
                "uniqueUsername",
                "testNameD",
                "testPasswordD",
                "testPasswordD",
                "uniqueNickname",
                "unique@email.com",
                "010-unique-number",
                "ROLE_ADMIN",
                "주소D"
        );

    }

    @AfterEach
    public void rollBack(){
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("member 생성 테스트")
    void t1() throws Exception{
        //given
        //when
        memberService.register(memberDTO);

        Member member1 = memberRepository.findByNickname(memberDTO.getNickname());
        MemberDTO memberDTO1 = new MemberDTO(member1);
        //then
        assertThat(member1.getNickname()).isEqualTo(memberDTO.getNickname());
        assertThat(memberDTO1).isEqualTo(memberDTO);
    }

    @Test
    @DisplayName("member 생성 에러 테스트")
    void MemberServiceTest1() {
        // given

        // when
            memberService.register(memberDTO);
        // then
        Assertions.assertThrows(RuntimeException.class,()->{
            memberService.register(memberDTO);
        });
    }

    @Test
    @DisplayName("myDetails 성공 테스트 ")
    void MemberServiceTest2() {
        // given
            MemberDTO testMemberDTO = memberService.myDetails(1L);
        // when
            MemberDTO memberDTO1 = memberService.myDetails(1L);
        // then
            assertThat(memberDTO1.getNickname()).isEqualTo(member.getNickname()+"sdf");
            System.out.println(memberDTO1.getNickname());
            assertThat(memberDTO1.getEmail()).isEqualTo(member.getEmail());
            assertThat(memberDTO1.getUsername()).isEqualTo(member.getUsername());
            assertThat(memberDTO1.getName()).isEqualTo(member.getUsername());
    }

}
