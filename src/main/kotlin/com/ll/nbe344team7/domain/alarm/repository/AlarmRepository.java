package com.ll.nbe344team7.domain.alarm.repository;

import com.ll.nbe344team7.domain.alarm.dto.AlarmDTO;
import com.ll.nbe344team7.domain.alarm.entity.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm,Long> {
    Page<AlarmDTO> findByMemberId(Pageable pageable, Long memberId);
}
