package com.payservice.repository;

import com.payservice.domain.Account;
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
class AccountRepositoryTest {

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
    @DisplayName("계좌 저장 및 조회 검증")
    void accountSaveAndFind(){
        List<Account> bfList = accountRepository.findAll();

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

        // when
        // 단건 조회 검증
        Account savedAccount = accountRepository.findById(account.getAccountNumber()).get();
        // 리스트 조회 검증
        List<Account> savedList = accountRepository.findAll();

        // then
        assertThat(savedList.size()).isEqualTo(bfList.size()+1);
        assertThat(savedAccount.getAccountNumber()).isEqualTo(accountNumber);
        assertThat(savedAccount.getUser().getId()).isEqualTo(user.getId());
    }

}