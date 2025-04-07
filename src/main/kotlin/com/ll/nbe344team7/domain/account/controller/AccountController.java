package com.ll.nbe344team7.domain.account.controller;

import com.ll.nbe344team7.domain.account.dto.AccountDTO;
import com.ll.nbe344team7.domain.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
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
    @Operation(summary = "보유금 조회")
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
    @Operation(summary = "거래 내역 조회")
    @GetMapping("/{id}/exchange")
    public ResponseEntity<?> getExchangeAccount(@PathVariable Long id, @RequestParam(name = "type") String type) {
        return ResponseEntity.ok(this.accountService.getExchangeAccount(id, type));
    }

    @Operation(summary = "계좌 생성")
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO account,
                                           @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(this.accountService.createAccount(account, user.getMemberId()));
    }
}
