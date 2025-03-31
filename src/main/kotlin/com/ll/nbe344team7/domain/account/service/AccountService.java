package com.ll.nbe344team7.domain.account.service;


import com.ll.nbe344team7.domain.account.dto.AccountDTO;
import com.ll.nbe344team7.domain.account.entity.Account;
import com.ll.nbe344team7.domain.account.repository.AccountRepository;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.domain.pay.entity.Payment;
import com.ll.nbe344team7.domain.pay.repository.PaymentRepository;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
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
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    public AccountService(AccountRepository accountRepository, PaymentRepository paymentRepository, MemberRepository memberRepository) {
        this.accountRepository = accountRepository;
        this.paymentRepository = paymentRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * 보유금 조회 기능
     *
     * @return
     * @author shjung
     * @since 25. 3. 24.
     */
    public Map<Object, Object> getAccount(Long memberId) {
        try{
            Account account = this.accountRepository.findByMemberId(memberId);
            return Map.of("money", account.getMoney());
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
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

    /**
     *
     * 계좌번호 및 은행 이름 저장
     *
     * @param dto
     * @return
     *
     * @author shjung
     * @since 25. 3. 31.
     */
    public Map<Object, Object> createAccount(AccountDTO dto) {
        if(!memberRepository.existsById(dto.getMemberId())) {
            throw new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER);
        }

        Account account;
        if(accountRepository.count() > 0) {
            account = this.accountRepository.findByMemberId(dto.getMemberId());
            account.setBankName(dto.getBankName());
            account.setAccountNumber(dto.getAccountNumber());
        }else {
            account = new Account(dto);
        }

        accountRepository.save(account);

        return Map.of("message", "계좌번호 및 은행이 저장되었습니다.");
    }
}