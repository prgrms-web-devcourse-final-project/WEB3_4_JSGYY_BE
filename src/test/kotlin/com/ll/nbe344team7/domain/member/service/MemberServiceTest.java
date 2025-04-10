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
                "testUsernameD",
                "testNameD",
                "testPasswordD",
                "testPasswordD",
                "testNicknameD",
                "test@email.comD",
                "010-test-testD",
                "ROLE_ADMIN",
                "주소D"
        );

        memberRepository.save(member);
    }

    @AfterEach
    public void tearDown(){
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("member 생성 테스트")
    void t1() throws Exception{
        memberService.register(memberDTO);

        Optional<Member> saveMember = memberRepository.findById(1L);

        assertThat(saveMember.isPresent()).isTrue();
        assertThat(saveMember.get().getUsername()).isEqualTo(memberDTO.getUsername());
        assertThat(saveMember.get().getEmail()).isEqualTo(memberDTO.getEmail());
        assertThat(saveMember.get().getNickname()).isEqualTo(memberDTO.getNickname());

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
    @DisplayName("")
    void MemberServiceTest2() {
        // given

        // when

        // then
    }

}
