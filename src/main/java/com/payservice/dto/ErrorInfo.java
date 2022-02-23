package com.payservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Schema(description = "에러 응답 값")
@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorInfo {
    @Schema(description = "에러 메세지")
    @NotEmpty
    private String message;
    @Schema(description = "에러 코드")
    @NotEmpty
    private String errorCode;


}
