package com.ll.nbe344team7.domain.alarm.controller;

import com.ll.nbe344team7.domain.alarm.dto.AlarmDTO;
import com.ll.nbe344team7.domain.alarm.service.AlarmService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
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
@Tag(name = "알람 API")
public class AlarmController {

    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    /**
     * 알람 목록조회
     * @param userDetails
     * @param page
     * @param size
     * @return ResponseEntity<Map<String,Object>>
     *
     * @author 이광석
     * @since 2025-04-03
     */
    @Operation(
            summary = "알람 목록 조회",
            parameters = {
                    @Parameter(name = "page", description = "페이지 번호", required = true, schema = @Schema(type = "integer", defaultValue = "1")),
                    @Parameter(name = "size", description = "페이지 당 알림 수", required = true, schema = @Schema(type = "integer", defaultValue = "10"))
            },
            responses = {
                @ApiResponse(responseCode = "200", description = "목록 조회 성공", content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = AlarmDTO.class)
                ))
            }
    )
    @GetMapping
    public ResponseEntity<Map<String,Object>> getAlarms(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value ="size" , defaultValue = "10" ) int size
            ){
        Page<AlarmDTO>  alarms = alarmService.findAll(page,size,userDetails.getMemberId());

        return ResponseEntity.ok(buildResponse("알람 전달 성공",alarms));
    }


    /**
     * 알람 삭제
     *
     * @param userDetails
     * @param id
     * @return ResponseEntity<Map<String,Object>>
     *
     * @author 이광석
     * @since 2025-04-03
     */
    @Operation(
            summary = "알람 삭제",
            parameters = {
                    @Parameter(name = "id", description = "알람 ID", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message": "알람 삭제 성공"
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "403", description = "권한 오류", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "권한이 없습니다."
                                            }
                                            """
                            )
                    ))
            }
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Map<String,Object>> deleteAlarm(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(value = "id")Long id
    ){
        Map<String,Object> result = new HashMap<>();

        if(!alarmService.checkAuthority(id,userDetails.getMemberId())){
            return ResponseEntity.status(403).body(buildResponse("권한이 없습니다" , null));
        }else {
            alarmService.delete(id);
            return  ResponseEntity.ok(buildResponse("알람 삭제 성공",null));
        }
    }


    @GetMapping("/create")
    public ResponseEntity< Map<String,Object>> createAlarm(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        alarmService.createAlarm("웹소켓 입니다.."+userDetails.getMemberId(),2L,2,1L);
        return ResponseEntity.ok(buildResponse("알람 생성 성공",null));
    }


    /**
     * repose 빌드 메소드
     *
     * @param message
     * @param data
     * @return Map<String,Object>
     *
     * @author 이광석
     * @since 2025-04-03
     */
    private Map<String,Object> buildResponse(String message,Object data){
        Map<String,Object> response = new HashMap<>();
        response.put("message", message);
        response.put("data" , data);
        return response;
    }



}
