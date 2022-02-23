package com.payservice.repository;

import com.payservice.domain.AccountDetails;
import com.payservice.domain.User;
import com.payservice.dto.balance.AccountBalanceQueryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountDetailsRepository extends JpaRepository<AccountDetails, Long> {

    @Query("select ad from AccountDetails ad join fetch ad.account a join fetch a.user u")
    List<AccountDetails> findAccountDetails(Pageable pageable);

    @Query("select new com.payservice.dto.balance.AccountBalanceQueryDto(ad.account, sum(ad.amount)) from AccountDetails ad join ad.account a "
            + "where a.user = :user AND ad.isDeposit = :isDeposit "
            + "group by ad.account")
    List<AccountBalanceQueryDto> findUserAccountFlow(@Param("user") User user, @Param("isDeposit") String isDeposit);
}
