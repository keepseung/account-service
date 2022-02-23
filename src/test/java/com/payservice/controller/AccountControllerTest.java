package com.payservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payservice.domain.User;
import com.payservice.dto.APIResponse;
import com.payservice.dto.account.AccountCreateDto;
import com.payservice.dto.account.AccountDto;
import com.payservice.dto.user.UserCreateDto;
import com.payservice.service.AccountService;
import com.payservice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("계좌 번호로 계좌 조회 요청")
    void getAccountById(){
        String name = "seung";
        Integer age = 25;

        UserCreateDto userCreateDto = UserCreateDto.builder()
                .name(name)
                .age(age)
                .build();
        User user = userService.create(userCreateDto);

        String accountNumber = "1234-567";
        AccountCreateDto accountCreateDto = AccountCreateDto.builder()
                .accountNumber(accountNumber)
                .userId(user.getId())
                .build();
        accountService.create(accountCreateDto);

        ResponseEntity<APIResponse> response = restTemplate.getForEntity("/api/account/"+accountNumber, APIResponse.class);
        AccountDto accountDto = objectMapper.convertValue(response.getBody().getData(), new TypeReference<>() {});

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(accountDto.getAccountNumber()).isEqualTo(accountNumber);
    }

    @Test
    @DisplayName("계좌 전체 조회 요청")
    void getAccountList(){
        String name = "seung";
        Integer age = 25;

        UserCreateDto userCreateDto = UserCreateDto.builder()
                .name(name)
                .age(age)
                .build();
        User user = userService.create(userCreateDto);

        String accountNumber = "1234-567";
        AccountCreateDto accountCreateDto = AccountCreateDto.builder()
                .accountNumber(accountNumber)
                .userId(user.getId())
                .build();
        accountService.create(accountCreateDto);

        ResponseEntity<APIResponse> response = restTemplate.getForEntity("/api/account?page=1size=20", APIResponse.class);
        List<AccountDto> accounts = objectMapper.convertValue(response.getBody().getData(), new TypeReference<>() {});

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(accounts.size()).isGreaterThan(1);
    }

    @Test
    @DisplayName("계좌 생성 요청")
    void createAccount(){
        String name = "seung";
        Integer age = 25;
        String accountNumber = "1234-567";

        UserCreateDto userCreateDto = UserCreateDto.builder()
                .name(name)
                .age(age)
                .build();
        User user = userService.create(userCreateDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();

        map.put("userId", user.getId());
        map.put("accountNumber", accountNumber);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);

        ResponseEntity<APIResponse> response = restTemplate.postForEntity("/api/account",request, APIResponse.class);
        AccountDto accountDto = objectMapper.convertValue(response.getBody().getData(), new TypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response).isNotNull();
        assertThat(accountDto.getAccountNumber()).isEqualTo(accountNumber);
    }

}