package com.payservice.controller;

import com.payservice.dto.account.AccountCreateDto;
import com.payservice.dto.account.AccountDto;
import com.payservice.dto.APIResponse;
import com.payservice.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RequestMapping("/api/account")
@RestController
public class AccountController {

    private final AccountService accountService;

    // 계좌 생성
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<AccountDto> createAccount(@RequestBody @Valid AccountCreateDto accountCreateDto) {
        return APIResponse.setResponse(accountService.create(accountCreateDto));
    }

    // 계좌 리스트 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public APIResponse<List<AccountDto>> getAccountList(Pageable pageable){
        return APIResponse.setResponse(accountService.getList(pageable));
    }

    // 계좌 조회
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{accountNumber}")
    public APIResponse<AccountDto> getAccount(@PathVariable String accountNumber){
        return APIResponse.setResponse(accountService.get(accountNumber));
    }

}
