package com.ll.nbe344team7.domain.alarm.controller;

import com.ll.nbe344team7.domain.alarm.dto.AlarmDTO;
import com.ll.nbe344team7.domain.alarm.service.AlarmService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jyson
 * @since 25. 3. 24.
 */
@RestController
@RequestMapping("/api/alarms")
public class AlarmController {

    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    // 전체 알람 페이징
    @GetMapping
    public ResponseEntity<Map<String,Object>> getAlarms(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value ="size" , defaultValue = "10" ) int size
            ){
        Page<AlarmDTO>  alarms = alarmService.findAll(page,size,userDetails.getMemberId());

        Map<String,Object> result = new HashMap<>();
        result.put("message","알람 전달 성공");
        result.put("data",alarms);
        return ResponseEntity.ok(result);
    }


    // 알람 삭제
    @DeleteMapping("{id}")
    public ResponseEntity<Map<String,Object>> deleteAlarm(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(value = "id")Long id
    ){
        Map<String,Object> result = new HashMap<>();

        if(!alarmService.checkAuthority(id,userDetails.getMemberId())){
            result.put("message","권한이 없습니다.");
            return ResponseEntity.status(403).body(result);
        }else {
            alarmService.delete(id);
            result.put("message","알람 삭제 성공");
            return  ResponseEntity.ok(result);
        }
    }


}
