package com.ll.nbe344team7.domain.account.service;

import org.springframework.stereotype.Service;

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


//        return Map.of("exchanges", list);
        return null;
    }
}