package com.ll.nbe344team7.domain.pay.service;

import com.ll.nbe344team7.domain.pay.dto.DepositDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@Service
public class PayService {

    public Map<Object, Object> depoitAccount(DepositDTO dto){


        return Map.of("message", "충전 요청이 확이되었습니다.");
    }
}
