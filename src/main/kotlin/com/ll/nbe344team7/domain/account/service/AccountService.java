package com.ll.nbe344team7.domain.account.service;


import com.ll.nbe344team7.domain.account.dto.AccountDTO;
import com.ll.nbe344team7.domain.account.entity.Account;
import com.ll.nbe344team7.domain.account.enums.ExchangeSearchType;
import com.ll.nbe344team7.domain.account.exception.AccountException;
import com.ll.nbe344team7.domain.account.exception.AccountExceptionCode;
import com.ll.nbe344team7.domain.account.repository.AccountRepository;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.domain.pay.entity.Exchange;
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
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
        Account account = this.accountRepository.findByMemberId(memberId);
        if(account == null) {
            throw new AccountException(AccountExceptionCode.NOT_FOUND_ACCOUNT);
        }
        return Map.of("money", account.getMoney());
    }

    /**
     * 거래 내역 조회 기능
     *
     * @param type
     * @return
     * @author shjung
     * @since 25. 3. 24.
     */
    public Map<Object, Object> getExchangeAccount(Long memberId, String type) {
        try{
            String exchangeType = String.valueOf(ExchangeSearchType.valueOf(type.toUpperCase()));
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
            List<Exchange> list;
            if (exchangeType.equals("sender")) {
                list = this.paymentRepository.findByMyIdAndExchangeType(memberId, 0);
            } else if (exchangeType.equals("receiver")) {
                list = this.paymentRepository.findByMyIdAndExchangeType(memberId, 1);
            } else {
                list = this.paymentRepository.findByMyId(memberId);
            }

            if(list.isEmpty()) {
                throw new AccountException(AccountExceptionCode.NOT_FOUND_EXCHANGE);
            }

            return Map.of("exchanges", list);
        } catch (IllegalArgumentException e) {
            throw new AccountException(AccountExceptionCode.NOT_TYPE_EXCHANGE);
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
    public Map<Object, Object> createAccount(AccountDTO dto, Long memberId) {
        Member member = this.memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

        Account account;
        if(accountRepository.count() > 0) {
            account = this.accountRepository.findByMemberId(memberId);
            account.setBankName(dto.getBankName());
            account.setAccountNumber(dto.getAccountNumber());
        }else {
            account = new Account(dto, memberId);
        }

        accountRepository.save(account);

        return Map.of("message", member.getNickname() + "님의 계좌번호 및 은행이 저장되었습니다.");
    }
}