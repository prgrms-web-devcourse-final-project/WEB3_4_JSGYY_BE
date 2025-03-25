package com.ll.nbe344team7.domain.alarm.service;

import com.ll.nbe344team7.domain.alarm.dto.AlarmDTO;
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

    public Map<Object, Object> getItems(String status, int page, int size) {
        List<AlarmDTO> list = new ArrayList<>();

        list.add(new AlarmDTO("안녕하세요", "CHAT", false, LocalDateTime.now()));
        list.add(new AlarmDTO("반갑습니다", "CHAT", false, LocalDateTime.now()));
        list.add(new AlarmDTO("경매 낙찰", "BID", false, LocalDateTime.now()));

        return Map.of("alarms", list,
                "page", page,
                "size", size,
                "status", status);
    }

    public Map<Object, Object> delete(long id) {
        return Map.of("message", "알람삭제완료");
    }

    public AlarmDTO getItem(long id) {
        return new AlarmDTO("알람", "CHAT", false, LocalDateTime.now());
    }
}
