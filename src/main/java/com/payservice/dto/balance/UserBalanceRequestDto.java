package com.payservice.dto.balance;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "예치금 조회 기간")
@Data
@NoArgsConstructor
public class UserBalanceRequestDto {
    @Schema(description = "예치금 조회 시작일", example = "2021-11-12")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Schema(description = "예치금 조회 종료일", example = "2021-12-12")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}
