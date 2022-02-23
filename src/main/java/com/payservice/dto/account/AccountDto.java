package com.payservice.dto.account;

import com.payservice.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDto {
    private String accountNumber;
    private String name;

    @Builder
    public AccountDto(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.name = account.getUser().getName();
    }
}
