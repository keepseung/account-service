package com.payservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payservice.domain.User;
import com.payservice.dto.APIResponse;
import com.payservice.dto.user.UserCreateDto;
import com.payservice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자 아이디 조회 요청")
    void getUserById(){
        String name = "seung";
        Integer age = 25;

        UserCreateDto userCreateDto = UserCreateDto.builder()
                .name(name)
                .age(age)
                .build();
        User user = userService.create(userCreateDto);
        assertThat(user).isNotNull();

        ResponseEntity<APIResponse> response = restTemplate.getForEntity("/api/user/"+user.getId(), APIResponse.class);
        User users = objectMapper.convertValue(response.getBody().getData(), new TypeReference<>() {});

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(users.getName()).isEqualTo(user.getName());
    }

    @Test
    @DisplayName("사용자 아이디 조회 요청")
    void getUserList(){
        String name = "seung";
        Integer age = 25;

        UserCreateDto userCreateDto = UserCreateDto.builder()
                .name(name)
                .age(age)
                .build();
        User user = userService.create(userCreateDto);
        assertThat(user).isNotNull();

        ResponseEntity<APIResponse> response = restTemplate.getForEntity("/api/user?page=1size=20", APIResponse.class);
        List<User> users = objectMapper.convertValue(response.getBody().getData(), new TypeReference<>() {});

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(users.size()).isGreaterThan(1);
    }

    @Test
    @DisplayName("사용자 생성 요청")
    void createUser(){
        String name = "seung";
        Integer age = 25;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();

        map.put("name", name);
        map.put("age", age);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);

        ResponseEntity<APIResponse> response = restTemplate.postForEntity("/api/user",request, APIResponse.class);
        User user = objectMapper.convertValue(response.getBody().getData(), new TypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response).isNotNull();
        assertThat(user.getName()).isEqualTo(name);
    }

}