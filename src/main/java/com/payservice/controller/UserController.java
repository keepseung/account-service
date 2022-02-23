package com.payservice.controller;

import com.payservice.domain.User;
import com.payservice.dto.APIResponse;
import com.payservice.dto.user.UserCreateDto;
import com.payservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@AllArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {

    private final UserService userService;

    // 사용자 생성
    @Operation(summary = "사용자 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "사용자 생성 성공")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public APIResponse<User> createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return APIResponse.<User>builder()
                .success(true)
                .data(userService.create(userCreateDto))
                .build();
    }

    // 사용자 리스트 조회
    @Operation(summary = "사용자 조회 with 페이징", description = "페이징을 기반으로 사용자 전체 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public APIResponse<List<User>> getUserList(Pageable pageable){
        return APIResponse.setResponse(userService.getList(pageable));
    }

    // 사용자 조회
    @Operation(summary = "사용자 조회", description = "id를 이용하여 사용자를 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public APIResponse<User> getUser(@PathVariable Long id){
        return APIResponse.setResponse(userService.get(id));
    }

}

