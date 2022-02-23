package com.payservice.controller;

import com.payservice.dto.balance.AccountAgeGroupBalanceDto;
import com.payservice.dto.balance.AccountBalanceDto;
import com.payservice.dto.balance.UserBalanceDto;
import com.payservice.dto.balance.UserBalanceRequestDto;
import com.payservice.dto.details.AccountDetailsCreateDto;
import com.payservice.dto.details.AccountDetailsDto;
import com.payservice.dto.APIResponse;
import com.payservice.service.AccountDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RequestMapping("/api/account-details")
@RestController
public class AccountDetailsController {

    private final AccountDetailsService accountDetailsService;

    // 계좌 내역 추가
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<AccountDetailsDto> createAccountDetails(@RequestBody @Valid AccountDetailsCreateDto accountDetailsCreateDto) {
        return APIResponse.setResponse(accountDetailsService.create(accountDetailsCreateDto));
    }

    // 계좌 내역 리스트 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public APIResponse<List<AccountDetailsDto>> getAccountDetailsList(Pageable pageable) {
        return APIResponse.setResponse(accountDetailsService.getList(pageable));
    }

    // 계좌 내역 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{accountDetailsId}")
    public APIResponse<AccountDetailsDto> getAccountDetails(@PathVariable Long accountDetailsId) {
        return APIResponse.setResponse(accountDetailsService.get(accountDetailsId));
    }

    // 사용자별 예치금 조회하기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/balance/user/{id}")
    public APIResponse<List<AccountBalanceDto>> getUserBalanceList(@PathVariable Long id) {
        return APIResponse.setResponse(accountDetailsService.getBalanceList(id));
    }

    // 연령대별 예치금 조회하기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/balance/agegroup")
    public APIResponse<List<AccountAgeGroupBalanceDto>> getAgeGroupBalanceList() {
        return APIResponse.setResponse(accountDetailsService.getAgeGroupBalanceList());
    }

    // 연도 예치금 총액 조회하기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/balance/year/{year}")
    public APIResponse<Long> getYearTotalBalance(@PathVariable Integer year) {
        return APIResponse.setResponse(accountDetailsService.getYearTotalBalance(year));
    }

    // 기간 내 예치금이 많은 사용자 리스트 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/balance/top")
    public APIResponse<List<UserBalanceDto>> getTopBalanceUser(@ModelAttribute @Valid UserBalanceRequestDto topBalanceUserRequestDto) {
        return APIResponse.setResponse(accountDetailsService.getTopBalanceUser(topBalanceUserRequestDto));
    }
}
