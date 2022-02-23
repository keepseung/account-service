package com.payservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payservice.domain.User;
import com.payservice.dto.APIResponse;
import com.payservice.dto.account.AccountCreateDto;
import com.payservice.dto.details.AccountDetailsCreateDto;
import com.payservice.dto.details.AccountDetailsDto;
import com.payservice.dto.user.UserCreateDto;
import com.payservice.service.AccountDetailsService;
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
class AccountDetailsControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountDetailsService accountDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("계좌 내역 아이디로 계좌 내역 조회 요청")
    void getAccountDetailsById(){
        Long amount = 1000L;
        String isDeposit = "Y";
        AccountDetailsDto accountDetailsDto = saveAccountDetail(amount, isDeposit);
        ResponseEntity<APIResponse> response = restTemplate.getForEntity("/api/account-details/"+accountDetailsDto.getId(), APIResponse.class);
        AccountDetailsDto accountDetails = objectMapper.convertValue(response.getBody().getData(), new TypeReference<>() {});

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(accountDetails.getAmount()).isEqualTo(amount);
    }



    @Test
    @DisplayName("계좌 내역 전체 조회 요청")
    void getAccountDetailsList(){
        Long amount = 1000L;
        String isDeposit = "Y";
        saveAccountDetail(amount, isDeposit);
        ResponseEntity<APIResponse> response = restTemplate.getForEntity("/api/account-details?page=1size=20", APIResponse.class);
        List<AccountDetailsDto> accountDetailsDtos = objectMapper.convertValue(response.getBody().getData(), new TypeReference<>() {});

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(accountDetailsDtos.size()).isGreaterThan(1);
    }

    @Test
    @DisplayName("계좌 생성 요청")
    void createAccountDetails(){
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

        Long amount = 1000L;
        String isDeposit = "Y";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();

        map.put("userId", user.getId());
        map.put("accountNumber", accountNumber);
        map.put("amount",amount);
        map.put("isDeposit",isDeposit);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);

        ResponseEntity<APIResponse> response = restTemplate.postForEntity("/api/account-details",request, APIResponse.class);
        AccountDetailsDto accountDetailsDto = objectMapper.convertValue(response.getBody().getData(), new TypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response).isNotNull();
        assertThat(accountDetailsDto.getAmount()).isEqualTo(amount);
    }

    private AccountDetailsDto saveAccountDetail(Long amount, String isDeposit){
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

        AccountDetailsCreateDto accountDetailsCreateDto = AccountDetailsCreateDto.builder()
                .amount(amount)
                .isDeposit(isDeposit)
                .userId(user.getId())
                .accountNumber(accountNumber)
                .build();
        return accountDetailsService.create(accountDetailsCreateDto);
    }


}