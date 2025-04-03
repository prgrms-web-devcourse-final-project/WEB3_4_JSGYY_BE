package com.ll.nbe344team7.domain.alarm.service;

import com.ll.nbe344team7.domain.alarm.dto.AlarmDTO;
import com.ll.nbe344team7.domain.alarm.entity.Alarm;
import com.ll.nbe344team7.domain.alarm.exception.AlarmException;
import com.ll.nbe344team7.domain.alarm.exception.AlarmExceptionCode;
import com.ll.nbe344team7.domain.alarm.repository.AlarmRepository;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.service.MemberService;
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

    public AlarmService(AlarmRepository alarmRepository, MemberService memberService){
        this.alarmRepository = alarmRepository;
        this.memberService = memberService;
    }

    public Page<AlarmDTO> findAll(int page,int size,Long memberId) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<AlarmDTO> alarmDTOS = alarmRepository.findByMemberId(pageable,memberId);
        return alarmDTOS;
    }

    public void delete(Long alarmId) {
        Alarm alarm = findById(alarmId);


        alarmRepository.delete(alarm);
    }

    private Alarm findById(Long id){
        Alarm alarm = alarmRepository.findById(id)
                .orElseThrow(()->new  AlarmException(AlarmExceptionCode.NOT_FOUND_ALARM));
        return alarm;
    }

    public boolean checkAuthority(Long alarmId, Long memberId){
        Alarm alarm = findById(alarmId);
        Member member = alarm.getMember();

        if(member.getId().equals(memberId)){
            return false;
        }else{
            return true;
        }
    }

}
