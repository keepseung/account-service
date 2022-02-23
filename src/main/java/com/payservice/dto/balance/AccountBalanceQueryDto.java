package com.payservice.dto.balance;

import com.payservice.domain.Account;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class AccountBalanceQueryDto {
    private String accountNumber; // 계좌번호
    private Long amountCount; // 금액 총합

    @QueryProjection
    public AccountBalanceQueryDto(Account account, Long amountCount) {
        this.accountNumber = account.getAccountNumber();
        this.amountCount = amountCount;
    }
}
