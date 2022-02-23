package com.payservice.dto.details;

import com.payservice.domain.AccountDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "계좌 내역 응답 값")
@Data
@AllArgsConstructor
public class AccountDetailsDto {
    @Schema(description = "계좌 내역 아이디")
    private Long id;
    @Schema(description = "계좌 번호")
    private String accountNumber;
    @Schema(description = "사용자 이름")
    private String name;
    @Schema(description = "입금은 'Y', 출금은 'N'")
    private String isDeposit;
    @Schema(description = "금액")
    private Long amount;
    @Schema(description = "입금 또는 출금일")
    private LocalDate depositDate;

    @Builder
    public AccountDetailsDto(AccountDetails accountDetails) {
        this.id = accountDetails.getId();
        this.accountNumber = accountDetails.getAccount().getAccountNumber();
        this.name = accountDetails.getAccount().getUser().getName();
        this.isDeposit = accountDetails.getIsDeposit();
        this.amount = accountDetails.getAmount();
        this.depositDate = accountDetails.getDepositDate();

    }

}
