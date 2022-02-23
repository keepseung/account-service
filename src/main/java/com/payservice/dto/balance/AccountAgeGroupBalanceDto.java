package com.payservice.dto.balance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "연령대 예치금 정보")
@Data
public class AccountAgeGroupBalanceDto {
    @Schema(description = "연령대", example = "10대는 10, 20대는 20")
    private Integer ageGroup; // 연령대
    @Schema(description = "예치금")
    private Long balance; // 예치금

    @Builder
    public AccountAgeGroupBalanceDto(Integer ageGroup, Long balance) {
        this.ageGroup = ageGroup;
        this.balance = balance;
    }
}
