package com.ll.nbe344team7.domain.account.service;

import com.ll.nbe344team7.domain.account.dto.ExchangeDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@Service
public class AccountService {

    /**
     *
     * 보유금 조회 기능
     *
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    public Map<Object, Object> getAccount() {
        return Map.of("money", 50000);
    }

    /**
     *
     * 거래 내역 조회 기능
     *
     * @param exchangeType
     * @return
     *
     * @author shjung
     * @since 25. 3. 24.
     */
    public Map<Object, Object> getExchangeAccount(String exchangeType) {
        List<ExchangeDTO> list = new ArrayList<>();

        list.add(new ExchangeDTO("출금", "2025.03.25.", 15000,35000,"이광석"));
        list.add(new ExchangeDTO("입금", "2025.03.26.", 50000,85000,"충전"));
        list.add(new ExchangeDTO("출금", "2025.03.26.", 50000,35000,"김재민"));
        list.add(new ExchangeDTO("출금", "2025.03.26.", 15000,20000,"박가은"));
        list.add(new ExchangeDTO("입금", "2025.03.27.", 15000,35000,"손진영"));
        list.add(new ExchangeDTO("출금", "2025.03.27.", 35000,0,"출금"));

        return Map.of("exchanges", list);
    }
}