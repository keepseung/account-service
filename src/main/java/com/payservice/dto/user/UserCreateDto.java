package com.payservice.dto.user;

import com.payservice.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@Schema(description = "사용자")
@Data
public class UserCreateDto {

    @Schema(description = "사용자 이름", example="keepseung")
    @NotBlank(message = "사용자 이름이 비어있습니다.")
    @NotNull
    private String name;

    @Schema(description = "사용자 나이", example="25")
    @NotNull(message = "나이가 비어있습니다.")
    @Min(value = 0, message = "나이는 0 이상입니다.")
    private Integer age;

    public User toEntity(){
        return User.builder()
                .name(name)
                .age(age)
                .createDate(LocalDate.now())
                .build();
    }

}
