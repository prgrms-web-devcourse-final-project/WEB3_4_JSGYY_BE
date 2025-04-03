package com.ll.nbe344team7.global.base.initData;

import com.ll.nbe344team7.domain.alarm.entity.Alarm;
import com.ll.nbe344team7.domain.alarm.repository.AlarmRepository;
import com.ll.nbe344team7.domain.alarm.service.AlarmService;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.domain.member.service.MemberService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class InitDataApplicationRunner implements ApplicationRunner {
    private final MemberRepository memberRepository;

    private final AlarmRepository alarmRepository;
    final private BCryptPasswordEncoder bCryptPasswordEncoder;
    public InitDataApplicationRunner(MemberRepository memberRepository,
                                     AlarmRepository alarmRepository,
                                     BCryptPasswordEncoder bCryptPasswordEncoder){
        this.memberRepository = memberRepository;
        this.alarmRepository=alarmRepository;
        this.bCryptPasswordEncoder =bCryptPasswordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(args.containsOption("initMode")){
            System.out.println("초기 모드 활성화");
        }
        String password = bCryptPasswordEncoder.encode("12345");
        if(memberRepository.count()==0) {

            Member member1 = new Member("admin1", "nickname1",password);
            Member member2 = new Member("admin2", "nickname2",password);
            Member member3 = new Member("admin3", "nickname3",password);
            Member member4 = new Member("admin4", "nickname4",password);
            Member member5 = new Member("admin5", "nickname5",password);


            memberRepository.save(member1);
            memberRepository.save(member2);
            memberRepository.save(member3);
            memberRepository.save(member4);
            memberRepository.save(member5);

            Alarm alarm1 = new Alarm(member1,"~님이 메시지를 보냈습니다",1);
            Alarm alarm2 = new Alarm(member1,"~에서 낙찰되었습니다",2);
            Alarm alarm3 = new Alarm(member1,"알림입니다",3);
            Alarm alarm4 = new Alarm(member2,"~님이 메시지를 보냈습니다",1);
            Alarm alarm5 = new Alarm(member2,"~에서 낙찰되었습니다",2);

            alarmRepository.save(alarm1);
            alarmRepository.save(alarm2);
            alarmRepository.save(alarm3);
            alarmRepository.save(alarm4);
            alarmRepository.save(alarm5);
        }
    }
}
