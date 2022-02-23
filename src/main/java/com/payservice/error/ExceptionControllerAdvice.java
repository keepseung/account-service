package com.payservice.error;

import com.payservice.dto.ErrorInfo;
import com.payservice.dto.APIResponse;
import com.payservice.error.exception.AccountDetailsNotExistException;
import com.payservice.error.exception.AccountNotExistException;
import com.payservice.error.exception.DuplicateAccountException;
import com.payservice.error.exception.UserNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.payservice.error.ErrorMessage.*;
import static com.payservice.error.ErrorMessage.VALIDATION_TYPE_BODY_ERROR_MSG;

/**
 * 4xx, 5xx 에러 발생시 클라이언트에게 전달할 응답, 상태코드를 처리함
 */
@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    private static final String SERVER_ERROR = "server.error";
    private static final String VALIDATION_ERROR = "validation.error";
    private static final String VALIDATION_TYPE_MISMATCH = "validation.type.mismatch";
    private static final String VALIDATION_TYPE_BODY_ERROR = "validation.type.error";

    // 유저 조회 예외 처리
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotExistException.class)
    public APIResponse<?> notExistAccountDetails(UserNotExistException ex) {
        return APIResponse.setErrorResponse(new ErrorInfo(UserNotExistException.errorCode, NOT_EXIST_USER));
    }

    // 계좌 조회 예외 처리
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNotExistException.class)
    public APIResponse<?> notExistAccount(AccountNotExistException ex) {
        return APIResponse.setErrorResponse(new ErrorInfo(AccountNotExistException.errorCode, NOT_EXIST_ACCOUNT));
    }

    // 계좌 내역 조회 예외 처리
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountDetailsNotExistException.class)
    public APIResponse<?> notExistUser(AccountDetailsNotExistException ex) {
        return APIResponse.setErrorResponse(
                ErrorInfo.builder()
                        .errorCode(AccountDetailsNotExistException.errorCode)
                        .message(ex.getLocalizedMessage())
                        .build()
        );
    }

    // 계좌 중복 예외 처리
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateAccountException.class)
    public APIResponse<?> duplicateAccount(DuplicateAccountException ex) {
        return APIResponse.setErrorResponse(
                ErrorInfo.builder()
                        .errorCode(DuplicateAccountException.errorCode)
                        .message(ex.getLocalizedMessage())
                        .build()
        );
    }

    // @ModelAttribute 나 @RequestBody처리를 위해 데이터 바인딩 중에 Validation 오류가 있을 경우 처리함
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public APIResponse<?> validationException(MethodArgumentNotValidException ex) {
        return APIResponse.setErrorResponse(
                ErrorInfo.builder()
                        .errorCode(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                        .message(VALIDATION_ERROR)
                        .build()
        );
    }

    // 타입 불일치로 Json 파싱 에러가 발생하는 경우
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public APIResponse<?> validationTypeException(HttpMessageNotReadableException ex) {
        return APIResponse.setErrorResponse(
                ErrorInfo.builder()
                        .errorCode(VALIDATION_TYPE_BODY_ERROR)
                        .message(VALIDATION_TYPE_BODY_ERROR_MSG)
                        .build()
        );
    }

    // @PathVariable에서 타입 불일치로 발생하는 예외 처리
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public APIResponse<?> validationTypeException(MethodArgumentTypeMismatchException ex) {
        return APIResponse.setErrorResponse(
                ErrorInfo.builder()
                        .errorCode(VALIDATION_TYPE_MISMATCH)
                        .message(TYPE_MISMATCH_MSG)
                        .build()
        );
    }



    // 서버 에러 처리
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public APIResponse<?> serverError(Exception ex) {
        log.error("error =" + ex);
        return APIResponse.setErrorResponse(
                ErrorInfo.builder()
                        .errorCode(SERVER_ERROR)
                        .message(INTERNAL_SERVER_ERROR)
                        .build()
        );
    }
}
