package com.ll.nbe344team7.domain.account.controller;

import com.ll.nbe344team7.domain.account.enums.ExchangeSearchType;
import com.ll.nbe344team7.domain.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        try {
            return ResponseEntity.ok(this.accountService.getAccount(id));
        } catch (NullPointerException e) {
            return ResponseEntity.status(404).body(Map.of("message", "멤버가 조회되지 않습니다."));
        }
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
        String exchangeType;
        try {
            exchangeType = String.valueOf(ExchangeSearchType.valueOf(type.toUpperCase()));
            return ResponseEntity.ok(this.accountService.getExchangeAccount(id, exchangeType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("message", "조회 타입이 올바르지 않습니다."));
        } catch (NullPointerException e) {
            String exchangeName = String.valueOf(ExchangeSearchType.valueOf(type.toUpperCase()).getType());
            return ResponseEntity.status(404).body(Map.of("message", "멤버의 " + exchangeName + " 거래 내역이 조회되지 않습니다."));
        }
    }
}
