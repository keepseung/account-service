package com.payservice.repository;

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
class UserRepositoryTest {

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
    @DisplayName("유저 저장 및 조회 검증")
    void userSaveAndFind(){
        List<User> bfList = userRepository.findAll();

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

        // when
        // 단건 조회 검증
        User savedUser = userRepository.findById(user.getId()).get();
        // 리스트 조회 검증
        List<User> savedList = userRepository.findAll();

        // then
        assertThat(savedList.size()).isEqualTo(bfList.size()+1);
        assertThat(savedUser.getName()).isEqualTo(name);
        assertThat(savedUser.getAge()).isEqualTo(age);
    }
}