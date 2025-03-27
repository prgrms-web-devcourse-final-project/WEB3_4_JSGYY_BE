package com.ll.nbe344team7.domain.pay.service;

import com.ll.nbe344team7.domain.pay.dto.DepositDTO;
import com.ll.nbe344team7.domain.pay.dto.WithdrawDTO;
import com.ll.nbe344team7.domain.pay.entity.Exchange;
import com.ll.nbe344team7.domain.pay.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

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
        IamportResponse<Payment> iamportResponse;
        try{
            iamportResponse = iamportClient.paymentByImpUid(dto.getImpUid());
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException(e);
        }
        Exchange exchange = new Exchange(dto);



        return Map.of("message", "충전 요청이 확이되었습니다.");
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
