package com.ll.nbe344team7.domain.account.repository;

import com.ll.nbe344team7.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author shjung
 * @since 25. 3. 25.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    public Account findByMemberId(Long memberid);
}
