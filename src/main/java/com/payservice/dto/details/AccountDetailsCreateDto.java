package com.payservice.dto.details;

import com.payservice.domain.Account;
import com.payservice.domain.AccountDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Schema(description = "계좌 내역 생성 정보")
@Builder
@Data
public class AccountDetailsCreateDto {

    @Schema(description = "계좌 번호")
    @NotBlank(message = "계좌 번호가 비어있습니다.")
    private String accountNumber;

    @Schema(description = "사용자 아이디")
    @NotNull(message = "사용자 아이디가 없습니다.")
    @Min(value = 1, message = "사용자 아이디는 최소 1 이상입니다.")
    private Long userId;

    @Schema(description = "입금은 'Y', 출금은 'N'")
    @NotNull(message = "입금 또는 출금 여부 값이 비어있습니다.")
    @Pattern(regexp = "Y|N", message = "입금은 'Y', 출금은 'N'입니다")
    private String isDeposit;

    @Schema(description = "금액")
    @NotNull(message = "금액이 없습니다.")
    @Min(value = 0, message = "금액은 최소 0원 이상입니다.")
    private Long amount;

    public AccountDetails toEntity(Account account){
        return AccountDetails.builder()
                .account(account)
                .isDeposit(isDeposit)
                .amount(amount)
                .depositDate(LocalDate.now())
                .build();
    }
}
