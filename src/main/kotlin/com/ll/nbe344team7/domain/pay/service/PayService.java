package com.ll.nbe344team7.domain.pay.service;

import com.ll.nbe344team7.domain.pay.dto.DepositDTO;
import com.ll.nbe344team7.domain.pay.dto.WithdrawDTO;
import com.ll.nbe344team7.domain.pay.entity.Exchange;
import com.ll.nbe344team7.domain.pay.exception.PayExceptionCode;
import com.ll.nbe344team7.domain.pay.exception.PaymentException;
import com.ll.nbe344team7.domain.pay.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@Service
public class PayService {

    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;

    @Value("${iamport.REST_API_KEY}")
    private final String REST_API_KEY = "";
    @Value("${iamport.REST_API_SECRET}")
    private final String REST_API_SECRET = "";

    public PayService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        this.iamportClient = new IamportClient(REST_API_KEY, REST_API_SECRET);
    }

    /**
     *
     * 충전 요청 기능
     *
     * @param dto
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    public Map<Object, Object> depositAccount(DepositDTO dto){
        try{
            // 1. api 결제 확인 요청
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(dto.getImpUid());
            // 2. 결제 금액 확인
            Long amount = (iamportResponse.getResponse().getAmount()).longValue();
            // 3. 결제 금액이 들어온 금액과 다를 경우 exception 발생
            if(!Objects.equals(dto.getPrice(), amount)){
                throw new PaymentException(PayExceptionCode.PRICE_ERROR);
            }
            // 4. 결제 상태 확인
            int status = (iamportResponse.getResponse().getStatus().equals("paid")) ? 1 : 0;
            // 5. 결제가 완료되지 않은 경우 exception 발생
            if(status == 0){
                throw new PaymentException(PayExceptionCode.PAYMENT_STATUS_ERROR);
            }
            // 6. 거래 내역 저장할 entity 생성
            Exchange exchange = new Exchange(dto, status);

            // 7. 만약 거래 내역이 저장된 상태면 exception 발생
            if(paymentRepository.countByImpUidContainsIgnoreCase(dto.getImpUid()) > 0){
                throw new PaymentException(PayExceptionCode.PAYMENT_ERROR);
            }
            // 8. 이상이 없는 경우 저장 후 리턴
            paymentRepository.save(exchange);
            return Map.of("message", "충전 요청이 확인되었습니다.");
        } catch (IamportResponseException | IOException e) {
            throw new PaymentException(PayExceptionCode.PORTONE_ERROR);
        }
    }

    /**
     *
     * 출금 요청 기능
     *
     * @param dto
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    public Map<Object, Object> withdrawAccount(WithdrawDTO dto){
        return Map.of("message", "출금 요청이 확인되었습니다.");
    }
}
