package com.ll.nbe344team7.domain.account.controller;

import com.ll.nbe344team7.domain.account.dto.AccountDTO;
import com.ll.nbe344team7.domain.account.dto.ExchangeDTO;
import com.ll.nbe344team7.domain.account.service.AccountService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
//
/**
 * @author shjung
 * @since 25. 3. 24.
 */
@RestController
@RequestMapping("/api/account")
@Tag(name = "계좌 API")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     *
     * 보유금 조회
     *
     * @param id
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    @Operation(
            summary = "보유금 조회",
            parameters = {
                    @Parameter(name = "id", description = "계좌 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "계좌 조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"money\": 10000 }"))),
                    @ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(this.accountService.getAccount(id));
    }

    /**
     *
     * 거래 내역 조회
     *
     * @param id
     * @param type
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    @Operation(
            summary = "거래 내역 조회",
            parameters = {
                    @Parameter(name = "id", description = "계좌 ID", required = true),
                    @Parameter(name = "type", description = "거래 타입(sender : 출금, receiver : 입금, all : 전체는 생략가능)")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "거래 내역 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExchangeDTO.class))),
                    @ApiResponse(responseCode = "404", description = "거래 내역을 찾을 수 없습니다.", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "조회 타입이 올바르지 않습니다.", content = @Content(mediaType = "application/json"))
            }

    )
    @GetMapping("/{id}/exchange")
    public ResponseEntity<?> getExchangeAccount(@PathVariable Long id, @RequestParam(name = "type") String type) {
        return ResponseEntity.ok(this.accountService.getExchangeAccount(id, type));
    }

    @Operation(
            summary = "계좌 생성",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "계좌 생성 DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "계좌 생성 또는 수정 성공", content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "성공 응답 예시",
                                            value = """
                                                    {
                                                      "message": "홍길동님의 계좌번호 및 은행이 저장되었습니다."
                                                    }
                                                    """
                                    ))
                    ),
                    @ApiResponse(responseCode = "404", description = "맴버가 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO account,
                                           @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(this.accountService.createAccount(account, user.getMemberId()));
    }
}
