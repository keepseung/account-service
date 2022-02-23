package com.payservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Schema(description = "기본 응답 값")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class APIResponse<T> {
    @Schema(description = "요청의 성공 여부를 나타냅니다.")
    @NotNull
    private Boolean success;
    @Schema(description = "응답 코드가 4xx, 5xx인 에러 상황일 때만 보입니다.")
    private ErrorInfo error;

    @Schema(description = "응답 코드가 2xx일 때만 보입니다.")
    private T data;

    /**
     * 성공했을 경우 응답 값을 생성함
     * @param t 사용자에게 전달한 응답 값
     * @param <T>
     * @return 응답 Json 데이터
     */
    public static<T> APIResponse<T> setResponse(T t) {
        return APIResponse.<T>builder()
                .data(t)
                .success(true)
                .build();
    }

    /**
     * 실패했을 경우 응답 값을 생성함
     * @param error 예외 핸들러에서 생성해주는 에러 메세지
     * @return 응답 Json 데이터
     */
    public static APIResponse<ErrorInfo> setErrorResponse(ErrorInfo error) {
        return APIResponse.<ErrorInfo>builder()
                .success(false)
                .error(error)
                .build();
    }
}
