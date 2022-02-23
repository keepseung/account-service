package com.payservice.repository;

import com.payservice.domain.Account;
import com.payservice.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface  AccountRepository extends JpaRepository<Account, String> {

    @Query("select a from Account a where a.accountNumber=:accountNumber and a.user =:user")
    Optional<Account> findAccount(@Param("accountNumber") String accountNumber, @Param("user") User user);

    @Query("select a from Account a join fetch a.user u")
    List<Account> findAccountAndUser(Pageable pageable);
}
