package com.payservice.dto.balance;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "유저 예치금 정보")
@Data
public class UserBalanceDto implements Comparable<UserBalanceDto>{
    @Schema(description = "유저 아이디")
    private Long userId; // 유저 아이디
    @Schema(description = "유저 이름")
    private String name; // 유저 이름
    @Schema(description = "예치금")
    private Long balance; // 예치금

    @Builder
    @QueryProjection
    public UserBalanceDto(Long userId, String name, Long balance) {
        this.userId = userId;
        this.name = name;
        this.balance = balance;
    }

    @Override
    public int compareTo(UserBalanceDto o) {
        if (this.balance < o.getBalance()) {
            return 1;
        } else if (this.balance > o.getBalance()) {
            return -1;
        }
        return 0;
    }
}
