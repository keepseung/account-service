package com.payservice.repository;

import com.payservice.domain.Account;
import com.payservice.domain.AccountDetails;
import com.payservice.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccountDetailsRepositoryTest{

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountDetailsRepository accountDetailsRepository;
    @Autowired
    UserRepository userRepository;

    @AfterEach
    void cleanUp(){
        accountDetailsRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("계좌 내역 저장 및 조회 검증")
    void accountDetailsSaveAndFind(){
        List<AccountDetails> bfList = accountDetailsRepository.findAll();

        // given
        String name = "seung";
        Integer age = 25;
        LocalDate createDate = LocalDate.of(2022,1,12);

        User user = User.builder()
                .name(name)
                .age(age)
                .createDate(createDate)
                .build();
        userRepository.save(user);

        String accountNumber = "1092-1123";
        Account account = Account.builder()
                .user(user)
                .accountNumber(accountNumber)
                .build();
        accountRepository.save(account);

        Long amount = 2000L;
        String isDeposit = "Y";
        LocalDate depositDate = LocalDate.of(2022,1,13);
        AccountDetails accountDetails = AccountDetails.builder()
                .depositDate(depositDate)
                .isDeposit(isDeposit)
                .account(account)
                .amount(amount)
                .build();
        accountDetailsRepository.save(accountDetails);

        // when
        // 단건 조회 검증
        AccountDetails savedAccountDetails = accountDetailsRepository.findById(accountDetails.getId()).get();
        // 리스트 조회 검증
        List<AccountDetails> savedList = accountDetailsRepository.findAll();

        // then
        assertThat(savedList.size()).isEqualTo(bfList.size()+1);
        assertThat(savedAccountDetails.getAmount()).isEqualTo(amount);
        assertThat(savedAccountDetails.getAccount().getAccountNumber()).isEqualTo(account.getAccountNumber());
    }
}