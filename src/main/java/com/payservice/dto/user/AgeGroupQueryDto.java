package com.payservice.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AgeGroupQueryDto {
    private Integer ageGroup; // 나이 그룹
    private Integer total; // 그룹에 속한 사람 총 인원

//    @QueryProjection
    public AgeGroupQueryDto(Integer ageGroup, Integer total) {
        this.ageGroup = ageGroup;
        this.total = total;
    }
}
