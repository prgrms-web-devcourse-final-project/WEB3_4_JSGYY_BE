package com.ll.nbe344team7.domain.alarm.controller;

import com.ll.nbe344team7.domain.alarm.service.AlarmService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<?> items(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ALL") String status
    ){

        return ResponseEntity.ok(this.alarmService.getItems(status, page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(
            @PathVariable long id
    ){
        if (id == 10000)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "알람을 찾을 수 없습니다."));

        return ResponseEntity.ok(this.alarmService.delete(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> item(
            @PathVariable long id
    ){
        if (id == 10000)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "알람을 찾을 수 없습니다."));

        return ResponseEntity.ok(this.alarmService.getItem(id));
    }
}
