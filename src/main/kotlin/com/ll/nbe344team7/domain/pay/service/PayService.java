package com.ll.nbe344team7.domain.pay.service;

import com.ll.nbe344team7.domain.pay.dto.DepositDTO;
import com.ll.nbe344team7.domain.pay.dto.WithdrawDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@Service
public class PayService {

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
