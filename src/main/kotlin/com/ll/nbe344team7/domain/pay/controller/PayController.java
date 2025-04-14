package com.ll.nbe344team7.domain.pay.controller;

import com.ll.nbe344team7.domain.pay.dto.DepositDTO;
import com.ll.nbe344team7.domain.pay.dto.PaymentDTO;
import com.ll.nbe344team7.domain.pay.dto.WithdrawDTO;
import com.ll.nbe344team7.domain.pay.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "충전 요청 기능",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "충전 DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DepositDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "충전 요청이 확인되었습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "맴버 조회 실패", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "맴버가 조회되지 않습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "요청금액, 결제금액 차이로 실패", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "요청한 금액과 결제된 금액이 다릅니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "결제 오류 실패", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "결제 오류입니다. 다시 시도해주세요."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "이미 결제 실패", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "이미 결제가 된 요청입니다."
                                            }
                                            """
                            )
                    )),
            }

    )
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
    @Operation(
            summary = "출금 요청 기능",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WithdrawDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "출금 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "출금 요청이 확인되었습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "맴버 조회 실패", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "맴버가 조회되지 않습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "계좌 조회 실패", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "계좌를 찾을 수 없습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "요청금액, 결제금액 차이로 실패", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "요청한 금액과 결제된 금액이 다릅니다."
                                            }
                                            """
                            )
                    )),
            }
    )
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
    @Operation(
            summary = "물품 결제 기능",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "결제 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "결제가 완료되었습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "게시글 조회 실패", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "게시글이 조회되지 않습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "이미 결제 실패", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "이미 결제가 된 요청입니다."
                                            }
                                            """
                            )
                    ))
            }
    )
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
    @Operation(
            summary = "물품 구매 확정 기능",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "구매 확정 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "구매 확정이 완료되었습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "게시글 조회 실패", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "게시글이 조회되지 않습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "이미 결제 실패", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "이미 결제가 된 요청입니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "결제 오류", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "결제 오류입니다. 다시 시도해주세요."
                                            }
                                            """
                            )
                    ))
            }
    )
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmExchange(@RequestBody PaymentDTO paymentDTO,
                                             @AuthenticationPrincipal CustomUserDetails user){
        return ResponseEntity.ok(this.payService.confirmExchange(paymentDTO, user.getMemberId()));
    }
}
