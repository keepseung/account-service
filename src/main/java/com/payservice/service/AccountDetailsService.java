package com.payservice.service;

import com.payservice.domain.Account;
import com.payservice.domain.AccountDetails;
import com.payservice.domain.User;
import com.payservice.dto.balance.*;
import com.payservice.dto.details.AccountDetailsCreateDto;
import com.payservice.dto.details.AccountDetailsDto;
import com.payservice.dto.user.AgeGroupQueryDto;
import com.payservice.error.ErrorMessage;
import com.payservice.error.exception.AccountDetailsNotExistException;
import com.payservice.error.exception.AccountNotExistException;
import com.payservice.repository.AccountBalanceRepository;
import com.payservice.repository.AccountDetailsRepository;
import com.payservice.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class AccountDetailsService {

    private final AccountDetailsRepository accountDetailsRepository;
    private final AccountBalanceRepository accountBalanceRepository;
    private final AccountRepository accountRepository;
    private final UserService userService;

    private final String DEPOSIT = "Y";
    private final String WITHDRAW = "N";

    @Transactional
    public AccountDetailsDto create(AccountDetailsCreateDto accountDetailsCreateDto) {
        // 사용자 아이디와 계좌 번호가 유효한 값인지 확인한 이후에 계좌를 생성한다.
        User user = userService.get(accountDetailsCreateDto.getUserId());
        Account account = accountRepository.findAccount(accountDetailsCreateDto.getAccountNumber(), user)
                .orElseThrow(() -> new AccountNotExistException(ErrorMessage.NOT_EXIST_ACCOUNT_DETAILS));
        AccountDetails accountDetails = accountDetailsRepository.save(accountDetailsCreateDto.toEntity(account));

        return AccountDetailsDto.builder()
                .accountDetails(accountDetails)
                .build();
    }

    @Transactional(readOnly = true)
    public List<AccountDetailsDto> getList(Pageable pageable) {
        return accountDetailsRepository.findAccountDetails(pageable)
                .stream()
                .map(accountDetails ->
                        AccountDetailsDto.builder()
                                .accountDetails(accountDetails)
                                .build()
                )
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AccountDetailsDto get(Long accountDetailsId) {
        AccountDetails accountDetails = accountDetailsRepository.findById(accountDetailsId)
                .orElseThrow(() -> new AccountDetailsNotExistException(ErrorMessage.NOT_EXIST_ACCOUNT_DETAILS));
        return AccountDetailsDto.builder()
                .accountDetails(accountDetails)
                .build();
    }

    public List<AccountBalanceDto> getBalanceList(Long id) {
        User user = userService.get(id);

        // 입금한 내역과 출금한 내역을 따로 가져와서 스트림을 사용해 예치금을 구한다.
        List<AccountBalanceQueryDto> userDeposits = accountDetailsRepository.findUserAccountFlow(user, DEPOSIT);
        List<AccountBalanceQueryDto> userWithdraws = accountDetailsRepository.findUserAccountFlow(user, WITHDRAW);
        List<AccountBalanceDto> userBalances = new ArrayList<>();
        for (AccountBalanceQueryDto userDeposit : userDeposits) {

            // 예치금, 해당 계좌번호로 출금한 경우가 없다면 입금액이 예치금이 된다.
            Long balance = userDeposit.getAmountCount();

            Optional<AccountBalanceQueryDto> withdrawOne = userWithdraws.stream()
                    .filter(userWithdraw -> userDeposit.getAccountNumber().equals(userWithdraw.getAccountNumber()))
                    .findFirst();

            // 출금한 경우가 있다면 입금액에서 출금액을 제외한 것을 예치금으로 한다.
            if (withdrawOne.isPresent()) {
                balance = userDeposit.getAmountCount() - withdrawOne.get().getAmountCount();
            }

            userBalances.add(AccountBalanceDto.builder()
                    .accountNumber(userDeposit.getAccountNumber())
                    .balance(balance)
                    .build());
        }

        return userBalances;
    }

    public List<AccountAgeGroupBalanceDto> getAgeGroupBalanceList() {

        List<AccountAgeGroupBalanceDto> accountAgeGroupBalanceDtos = new ArrayList<>();
        List<AgeGroupQueryDto> ageGroupList = accountBalanceRepository.getAgeGroupList();
        log.info("sizeee "+ageGroupList.size());
        // 나이 그룹 마다 인츨, 출금한 경우를 조회해서 평균값을 만든다.
        for (AgeGroupQueryDto ageGroupQueryDto : ageGroupList) {
            Integer startAge = ageGroupQueryDto.getAgeGroup();

            // 총 입금 금액, 총 출금 금액을 구하고 뺄셈을 한 이후 나눗셈을 통해 평균 예치금을 구한다.
            int endAge = startAge + 9;
            log.info("startAge "+startAge+", end:"+endAge+", de"+DEPOSIT);
            List<Long> depositList = accountBalanceRepository.getAccountFlow(startAge, endAge, DEPOSIT);

            long depositSum = depositList.stream().mapToLong(Long::longValue).sum();
            List<Long> withdrawList = accountBalanceRepository.getAccountFlow(startAge, endAge, WITHDRAW);
            long withdrawSum = withdrawList.stream().mapToLong(Long::longValue).sum();
            long ageBalance = (depositSum - withdrawSum) / ageGroupQueryDto.getTotal();

            accountAgeGroupBalanceDtos.add(AccountAgeGroupBalanceDto.builder()
                    .ageGroup(startAge)
                    .balance(ageBalance).build());
        }

        return accountAgeGroupBalanceDtos;
    }

    /**
     * 특정 연도에 발생한 예치금을 구한다.
     *
     * @param year 연도
     * @return
     */
    public Long getYearTotalBalance(Integer year) {

        Long totalDeposit = accountBalanceRepository.getAccountFlowByYear(year, DEPOSIT);
        Long totalWithdraw = accountBalanceRepository.getAccountFlowByYear(year, WITHDRAW);

        // 조회된 금액이 없으면 0으로 한다.
        if (totalDeposit == null) {
            totalDeposit = 0L;
        }
        if (totalWithdraw == null) {
            totalWithdraw = 0L;
        }
        return totalDeposit - totalWithdraw;
    }

    public List<UserBalanceDto> getTopBalanceUser(UserBalanceRequestDto topBalanceUserRequestDto) {
        List<UserBalanceDto> userBalanceDtos = new ArrayList<>();

        List<UserBalanceDto> userDeposits = accountBalanceRepository.getBalance(topBalanceUserRequestDto, DEPOSIT);
        List<UserBalanceDto> userWithdraws = accountBalanceRepository.getBalance(topBalanceUserRequestDto, WITHDRAW);

        for (UserBalanceDto userDeposit : userDeposits) {

            UserBalanceDto userBalanceDto = UserBalanceDto.builder()
                    .userId(userDeposit.getUserId())
                    .name(userDeposit.getName())
                    .build();

            Optional<UserBalanceDto> findUserWithdraw = userWithdraws.stream().filter(userWithdraw -> userDeposit.getUserId().equals(userWithdraw.getUserId())).findFirst();

            if (findUserWithdraw.isPresent()){
                userBalanceDto.setBalance(userDeposit.getBalance() - findUserWithdraw.get().getBalance());
            }else{
                userBalanceDto.setBalance(userDeposit.getBalance());
            }
            userBalanceDtos.add(userBalanceDto);
        }

        Collections.sort(userBalanceDtos);
        return userBalanceDtos;
    }
}
