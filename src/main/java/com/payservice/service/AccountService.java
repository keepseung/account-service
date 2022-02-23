package com.payservice.service;

import com.payservice.domain.Account;
import com.payservice.domain.User;
import com.payservice.dto.account.AccountCreateDto;
import com.payservice.dto.account.AccountDto;
import com.payservice.error.ErrorMessage;
import com.payservice.error.exception.AccountNotExistException;
import com.payservice.error.exception.DuplicateAccountException;
import com.payservice.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AccountService {

    private final UserService userService;
    private final AccountRepository accountRepository;

    @Transactional
    public AccountDto create(AccountCreateDto accountCreateDto) {
        User user = userService.get(accountCreateDto.getUserId());

        // 이미 같은 사용자로 저장한 계좌가 있는지 중복 확인한다.
        Optional<Account> savedAccount = accountRepository.findAccount(accountCreateDto.getAccountNumber(), user);
        if (savedAccount.isPresent()){
            throw new DuplicateAccountException(ErrorMessage.DUPLICATE_ACCOUNT);
        }
        Account account = accountRepository.save(accountCreateDto.toEntity(user));

        return AccountDto.builder().account(account).build();
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getList(Pageable pageable) {
        return accountRepository.findAccountAndUser(pageable)
                .stream()
                .map(account -> AccountDto.builder().account(account).build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AccountDto get(String accountNumber) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotExistException(ErrorMessage.NOT_EXIST_ACCOUNT));

        return AccountDto.builder()
                .account(account)
                .build();
    }
}
