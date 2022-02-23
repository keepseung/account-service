package com.payservice.dto.account;

import com.payservice.domain.Account;
import com.payservice.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Schema(description = "계좌 생성 정보")
@Builder
@Data
public class AccountCreateDto {

    @Schema(description = "계좌 번호")
    @NotBlank(message = "계좌 번호가 비어있습니다.")
    private String accountNumber;

    @Schema(description = "사용자 아이디")
    @NotNull(message = "사용자 아이디가 없습니다.")
    @Min(value = 1, message = "사용자 아이디는 최소 1 이상입니다.")
    private Long userId;

    public Account toEntity(User user){
        return Account.builder()
                .accountNumber(accountNumber)
                .user(user)
                .build();
    }
}
