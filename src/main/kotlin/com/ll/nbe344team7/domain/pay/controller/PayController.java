package com.ll.nbe344team7.domain.pay.controller;

import com.ll.nbe344team7.domain.pay.dto.DepositDTO;
import com.ll.nbe344team7.domain.pay.dto.PaymentDTO;
import com.ll.nbe344team7.domain.pay.dto.WithdrawDTO;
import com.ll.nbe344team7.domain.pay.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@RestController
@RequestMapping("/api/pay")
@Tag(name = "결제 API")
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
    @Operation(summary = "충전 요청 기능")
    @PostMapping("/deposit")
    public ResponseEntity<?> depositAccount(@RequestBody DepositDTO depositDTO,
                                            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(this.payService.depositAccount(depositDTO, user.getMemberId()));
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
    @Operation(summary = "출금 요청 기능")
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawAccount(@RequestBody WithdrawDTO withdrawDTO,
                                             @AuthenticationPrincipal CustomUserDetails user){
        return ResponseEntity.ok(this.payService.withdrawAccount(withdrawDTO, user.getMemberId()));
    }

    /**
     *
     * 물품 결제 기능
     *
     * @param paymentDTO
     * @return
     *
     * @author shjung
     * @since 25. 4. 2.
     */
    @Operation(summary = "물품 결제 기능")
    @PostMapping
    public ResponseEntity<?> paymentsGood(@RequestBody PaymentDTO paymentDTO,
                                          @AuthenticationPrincipal CustomUserDetails user){
        return ResponseEntity.ok(this.payService.payExchange(paymentDTO, user.getMemberId()));
    }

    /**
     *
     * 물품 구매 확정 기능
     *
     * @param paymentDTO
     * @return
     *
     * @author shjung
     * @since 25. 4. 2.
     */
    @Operation(summary = "물품 구매 확정 기능")
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmExchange(@RequestBody PaymentDTO paymentDTO,
                                             @AuthenticationPrincipal CustomUserDetails user){
        return ResponseEntity.ok(this.payService.confirmExchange(paymentDTO, user.getMemberId()));
    }
}
