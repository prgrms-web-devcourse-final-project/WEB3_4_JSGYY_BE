package com.ll.nbe344team7.domain.pay.service;

import com.ll.nbe344team7.domain.account.entity.Account;
import com.ll.nbe344team7.domain.account.exception.AccountException;
import com.ll.nbe344team7.domain.account.exception.AccountExceptionCode;
import com.ll.nbe344team7.domain.account.repository.AccountRepository;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.domain.pay.dto.DepositDTO;
import com.ll.nbe344team7.domain.pay.dto.PaymentDTO;
import com.ll.nbe344team7.domain.pay.dto.WithdrawDTO;
import com.ll.nbe344team7.domain.pay.entity.Exchange;
import com.ll.nbe344team7.domain.pay.entity.Withdraw;
import com.ll.nbe344team7.domain.pay.exception.PayExceptionCode;
import com.ll.nbe344team7.domain.pay.exception.PaymentException;
import com.ll.nbe344team7.domain.pay.repository.PaymentRepository;
import com.ll.nbe344team7.domain.pay.repository.WithdrawRepository;
import com.ll.nbe344team7.domain.post.entity.Post;
import com.ll.nbe344team7.domain.post.repository.PostRepository;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private final WithdrawRepository withdrawRepository;
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    @Value("${iamport.REST_API_KEY}")
    private String REST_API_KEY;
    @Value("${iamport.REST_API_SECRET}")
    private String REST_API_SECRET;

    public PayService(PaymentRepository paymentRepository, WithdrawRepository withdrawRepository,
                      MemberRepository memberRepository, AccountRepository accountRepository,
                      PostRepository postRepository) {
        this.paymentRepository = paymentRepository;
        this.iamportClient = new IamportClient(REST_API_KEY, REST_API_SECRET);
        this.withdrawRepository = withdrawRepository;
        this.memberRepository = memberRepository;
        this.accountRepository = accountRepository;
        this.postRepository = postRepository;
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
    public Map<Object, Object> depositAccount(DepositDTO dto, Long memberId) {
        try{
            Member member = this.memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
            // 1. api 결제 확인 요청
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(dto.getImpUid());
            // 2. 결제 금액 확인
            Long amount = (iamportResponse.getResponse().getAmount()).longValue();
            // 3. 결제 금액이 들어온 금액과 다를 경우 exception 발생
            if(!Objects.equals(dto.getPrice(), amount)){
                iamportClient.cancelPaymentByImpUid(new CancelData(dto.getImpUid(), true));
                throw new PaymentException(PayExceptionCode.PRICE_ERROR);
            }
            // 4. 결제 상태 확인
            int status = (iamportResponse.getResponse().getStatus().equals("paid")) ? 1 : 0;
            // 5. 결제가 완료되지 않은 경우 exception 발생
            if(status == 0){
                throw new PaymentException(PayExceptionCode.PAYMENT_STATUS_ERROR);
            }
            // 6. 거래 내역 저장할 entity 생성
            Exchange exchange = new Exchange(dto, status, memberId);

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
    public Map<Object, Object> withdrawAccount(WithdrawDTO dto, Long memberId) {
        // 1. 멤버 오류 및 계좌 오류
        Member member = this.memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
        Account account = this.accountRepository.findByMemberId(memberId);

        if(account == null){
            throw new AccountException(AccountExceptionCode.NOT_FOUND_ACCOUNT);
        }

        // 2. 보유금 보다 큰 금액 출금 요청할 경우
        if(dto.getPrice() > account.getMoney()){
            throw new PaymentException(PayExceptionCode.PRICE_ERROR);
        }

        // 3. 출금 요청 저장
        Withdraw withdraw = new Withdraw(null, member.getName(), dto.getPrice(), account.getBankName(), account.getAccountNumber(), LocalDateTime.now());

        // 4. 거래 내역에 출금 저장
        Exchange exchange = new Exchange(null, memberId, memberId,
                LocalDateTime.now(), dto.getPrice(), 0, 1, null, null);
        account.setMoney(account.getMoney() - dto.getPrice());

        this.withdrawRepository.save(withdraw);
        this.accountRepository.save(account);
        this.paymentRepository.save(exchange);
        return Map.of("message", "출금 요청이 확인되었습니다.");
    }

    /**
     *
     * 결제 함수
     * - 현재 결제만 하고 구매확정은 안한 상태로 저장
     *
     * @param dto
     * @return
     *
     * @author shjung
     * @since 25. 4. 2.
     */
    public Map<Object, Object> payExchange(PaymentDTO dto, Long memberId) {
        // 1. 멤버 조회 및 게시글, 계좌 조회
        Member member = this.memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
        Post post = this.postRepository.findById(dto.getPostId()).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_POST));

        Account account = this.accountRepository.findByMemberId(memberId);
        if(account == null){
            throw new AccountException(AccountExceptionCode.NOT_FOUND_ACCOUNT);
        }
        // 2. 보유금보다 결제 금액이 높을 경우 에러
        if(post.getPrice() > account.getMoney()){
            throw new PaymentException(PayExceptionCode.PAYMENT_ERROR);
        }
        // 3. 송금 기록 저장
        //  - 구매 확정이 아니라서 결제 상태를 미 결제로 저장
        Exchange sendExchange = new Exchange(null, memberId, post.getMember().getId(),
                LocalDateTime.now(), post.getPrice(), 0, 1, null, dto.getPostId());
        post.updateSaleStatus(false);

        // 4. 보유금를 결제 금액을 제외하고 저장하고 송금 기록 및 보유금 수정
        account.setMoney(account.getMoney() - post.getPrice());
        accountRepository.save(account);
        paymentRepository.save(sendExchange);
        postRepository.save(post);

        return Map.of("message", post.getTitle() + " 결제가 완료되었습니다.");
    }

    /**
     *
     * 구매 확정 함수
     *
     * @param dto
     * @return
     *
     * @author shjung
     * @since 25. 4. 2.
     */
    public Map<Object, Object> confirmExchange(PaymentDTO dto, Long memberId) {
        // 1. 멤버 조회 및 게시글, 계좌 조회
        Member member = this.memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
        Post post = this.postRepository.findById(dto.getPostId()).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_POST));

        // 2. 구매한 물품 조회
        Exchange sendExchange = this.paymentRepository.findByMyIdAndPostId(memberId, dto.getPostId());
        if(sendExchange == null){
            throw new PaymentException(PayExceptionCode.PAYMENT_ERROR);
        }
        // 3. 구매 확정이 난 경우
        // - 이미 결제 상태가 완료된 경우 에러 발생
        if(sendExchange.getStatus() == 1){
            throw new PaymentException(PayExceptionCode.PAYMENT_STATUS_ERROR);
        }
        // 4. 결제 상태 완료로 변경 및 판매자는 입금 내역 추가
        sendExchange.setStatus(1);
        Exchange receiveExchange = new Exchange(null, post.getMember().getId(), memberId,
                LocalDateTime.now(), post.getPrice(), 1, 0, null, dto.getPostId());
        // 5. 판매자의 보유금 증가
        Account account = this.accountRepository.findByMemberId(post.getMember().getId());
        account.setMoney(account.getMoney() + post.getPrice());

        // 6. 변경된 거래 사항 및 보유금 저장
        this.paymentRepository.save(sendExchange);
        this.paymentRepository.save(receiveExchange);
        this.accountRepository.save(account);

        return Map.of("message", post.getTitle() + " 구매 확정이 완료되었습니다.");
    }
}
