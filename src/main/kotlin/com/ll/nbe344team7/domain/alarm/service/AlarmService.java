package com.ll.nbe344team7.domain.alarm.service;

import com.ll.nbe344team7.domain.alarm.dto.AlarmDTO;
import com.ll.nbe344team7.domain.alarm.entity.Alarm;
import com.ll.nbe344team7.domain.alarm.exception.AlarmException;
import com.ll.nbe344team7.domain.alarm.exception.AlarmExceptionCode;
import com.ll.nbe344team7.domain.alarm.repository.AlarmRepository;
import com.ll.nbe344team7.domain.member.dto.MemberDTO;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.service.MemberService;
import com.ll.nbe344team7.global.config.redis.publisher.AlarmRedisPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jyson
 * @since 25. 3. 24.
 */
@Service
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final MemberService memberService;

    private final AlarmRedisPublisher alarmRedisPublisher;

    public AlarmService(AlarmRepository alarmRepository,
                        MemberService memberService,
                        AlarmRedisPublisher alarmRedisPublisher){
        this.alarmRepository = alarmRepository;
        this.memberService = memberService;
        this.alarmRedisPublisher = alarmRedisPublisher;
    }

    /**
     * 알람 조회 메소드
     * 페이징
     * 최근 생성 순
     *
     * @param page
     * @param size
     * @param memberId
     * @return Page<AlarmDTO>
     * @author 이광석
     * @since 2025-04-03
     */
    public Page<AlarmDTO> findAll(int page,int size,Long memberId) {
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("createdAt").descending());
        Page<Alarm> alarms = alarmRepository.findByMemberId(pageable,memberId);


        return alarms.map(alarm -> new AlarmDTO(
              alarm
        ));
    }

    /**
     * 알람 삭제 메소드
     *
     * @param alarmId
     *
     * @author 이광석
     * @since 2025-04-03
     */
    public void delete(Long alarmId) {
        Alarm alarm = findById(alarmId);
        alarmRepository.delete(alarm);
    }

    /**
     * 알람 생성 메소드
     *
     * @param content
     * @param memberId
     * @param type
     *
     * @author 이광석
     * @since 2025-04-03
     */
    public void createAlarm(String content,Long memberId,int type){

        Member member = memberService.getMember(memberId);
        Alarm newAlarm = new Alarm(member,content,type);
        alarmRepository.save(newAlarm);

        alarmRedisPublisher.publishMessage(new AlarmDTO(newAlarm));
    }

    /**
     * 특정 알람 조회 메소드
     *
     * @param id
     * @return Alarm
     *
     * @author 이광석
     * @since 2025-04-03
     */
    private Alarm findById(Long id){
        return alarmRepository.findById(id)
                .orElseThrow(()->new  AlarmException(AlarmExceptionCode.NOT_FOUND_ALARM));

    }

    /**
     * 권환 확인 메소드
     * 알람의 memberid 와 현제 사용자의 memberid 비교
     *
     * @param alarmId
     * @param memberId
     * @return boolean
     * @author 이광석
     * @since 2025-04-03
     */
    public boolean checkAuthority(Long alarmId, Long memberId){
        Alarm alarm = findById(alarmId);
        return alarm.getMember().getId().equals(memberId);
    }



}
