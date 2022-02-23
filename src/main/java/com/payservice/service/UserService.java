package com.payservice.service;

import com.payservice.domain.User;
import com.payservice.dto.user.UserCreateDto;
import com.payservice.error.ErrorMessage;
import com.payservice.error.exception.UserNotExistException;
import com.payservice.repository.AccountDetailsRepository;
import com.payservice.repository.AccountRepository;
import com.payservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountDetailsRepository accountDetailsRepository;

    @Transactional
    public User create(UserCreateDto userCreateDto) {
        return userRepository.save(userCreateDto.toEntity());
    }

    @Transactional(readOnly = true)
    public List<User> getList(Pageable pageable){
        return userRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new UserNotExistException(ErrorMessage.NOT_EXIST_USER));
    }


}
