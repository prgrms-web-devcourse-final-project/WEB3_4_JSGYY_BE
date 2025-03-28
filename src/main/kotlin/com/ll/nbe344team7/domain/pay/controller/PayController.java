package com.ll.nbe344team7.domain.pay.controller;

import com.ll.nbe344team7.domain.pay.dto.DepositDTO;
import com.ll.nbe344team7.domain.pay.dto.PayDTO;
import com.ll.nbe344team7.domain.pay.dto.WithdrawDTO;
import com.ll.nbe344team7.domain.pay.service.PayService;
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
@RequestMapping("/api/pay")
public class PayController {

    private final PayService payService;

    public PayController(PayService payService) {
        this.payService = payService;
    }

    /**
     *
     * 충전 요청 기능
     *
     * @param depositDTO
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    @PostMapping("/deposit")
    public ResponseEntity<?> depositAccount(@RequestBody DepositDTO depositDTO){
        if(depositDTO.getMemberId() == null){
            return ResponseEntity.status(404).body(Map.of("message", "멤버가 조회되지 않습니다."));
        }
        else if(depositDTO.getPrice()==null || depositDTO.getPrice() == 0){
            return ResponseEntity.status(404).body(Map.of("message", "충전 요금이 존재하지 않습니다."));
        }
        else if(depositDTO.getImpUid().isBlank()){
            return ResponseEntity.status(404).body(Map.of("message", "결제 사항이 확인되지 않습니다."));
        }
        return ResponseEntity.ok(this.payService.depositAccount(depositDTO));
    }

    /**
     *
     * 출금 요청 기능
     *
     * @param withdrawDTO
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawAccount(@RequestBody WithdrawDTO withdrawDTO){
        if(withdrawDTO.getMemberId() == null){
            return ResponseEntity.status(404).body(Map.of("message", "멤버가 조회되지 않습니다."));
        }
        else if(withdrawDTO.getPrice() == null || withdrawDTO.getPrice() > 50000){
            return ResponseEntity.status(404).body(Map.of("message", "보유금의 한도를 넘어선 금액입니다."));
        }
        else if(withdrawDTO.getImpUid().isBlank()){
            return ResponseEntity.status(404).body(Map.of("message", "결제 사항이 확인되지 않습니다."));
        }
        return ResponseEntity.ok(this.payService.withdrawAccount(withdrawDTO));
    }

    @PostMapping
    public ResponseEntity<?> pay(@RequestBody PayDTO payDTO){
        if(payDTO.getMemberId() == null || payDTO.getSellerId() == null){
            return ResponseEntity.status(404).body(Map.of("message", "멤버가 조회되지 않습니다."));
        }
        else if(payDTO.getPrice() == 0){
            return ResponseEntity.status(400).body(Map.of("message", "물품의 가격과 일치하지 않습니다."));
        }
        return ResponseEntity.ok(Map.of("message", "결제 확인 되었습니다."));
    }
}
