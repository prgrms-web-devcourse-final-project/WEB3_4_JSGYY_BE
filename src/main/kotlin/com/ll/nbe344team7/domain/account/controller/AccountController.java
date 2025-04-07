package com.ll.nbe344team7.domain.account.controller;

import com.ll.nbe344team7.domain.account.dto.AccountDTO;
import com.ll.nbe344team7.domain.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//
/**
 * @author shjung
 * @since 25. 3. 24.
 */
@RestController
@RequestMapping("/api/account")
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
    @GetMapping("/{id}/exchange")
    public ResponseEntity<?> getExchangeAccount(@PathVariable Long id, @RequestParam(name = "type") String type) {
        return ResponseEntity.ok(this.accountService.getExchangeAccount(id, type));
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO account) {
        return ResponseEntity.ok(this.accountService.createAccount(account));
    }
}
