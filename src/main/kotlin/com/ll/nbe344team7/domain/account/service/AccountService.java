package com.ll.nbe344team7.domain.account.service;

import com.ll.nbe344team7.domain.pay.entity.Payment;
import com.ll.nbe344team7.domain.pay.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@Service
public class AccountService {

    private final PaymentRepository paymentRepository;

    public AccountService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * 보유금 조회 기능
     *
     * @return
     * @author shjung
     * @since 25. 3. 24.
     */
    public Map<Object, Object> getAccount() {
        return Map.of("money", 50000);
    }

    /**
     * 거래 내역 조회 기능
     *
     * @param exchangeType
     * @return
     * @author shjung
     * @since 25. 3. 24.
     */
    public Map<Object, Object> getExchangeAccount(Long memberId, String exchangeType) {
        try {
            List<Payment> list;
            if (exchangeType.equals("sender")) {
                list = this.paymentRepository.findByMyIdAndExchangeType(memberId, 0);
            } else if (exchangeType.equals("receiver")) {
                list = this.paymentRepository.findByMyIdAndExchangeType(memberId, 1);
            } else {
                list = this.paymentRepository.findByMyId(memberId);
            }

            if(list.isEmpty()) {
                throw new NullPointerException();
            }

            return Map.of("exchanges", list);
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
    }
}