package com.ll.nbe344team7.pay.controller;

import com.ll.nbe344team7.pay.dto.DepositDTO;
import com.ll.nbe344team7.pay.service.PayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@RestController
@RequestMapping("/api/pay/deposit")
public class PayController {

    private final PayService payService;

    public PayController(PayService payService) {
        this.payService = payService;
    }

    @PostMapping
    public ResponseEntity<?> depositAccount(@RequestBody DepositDTO depositDTO){
        if(depositDTO.getPrice()==0){
            return ResponseEntity.status(404).body(Map.of("message", "충전 요금이 존재하지 않습니다."));
        }
        return ResponseEntity.ok(this.payService.depoitAccount(depositDTO));
    }
}
