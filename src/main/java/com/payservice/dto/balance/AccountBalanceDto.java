package com.payservice.dto.balance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "예치금 정보")
@Data
public class AccountBalanceDto {
    @Schema(description = "계좌 번호")
    private String accountNumber; // 계좌번호
    @Schema(description = "예치금")
    private Long balance; // 예치금

    @Builder
    public AccountBalanceDto(String accountNumber, Long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
}
