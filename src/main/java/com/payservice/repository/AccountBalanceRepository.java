package com.payservice.repository;

import com.payservice.dto.balance.QUserBalanceDto;
import com.payservice.dto.balance.UserBalanceDto;
import com.payservice.dto.balance.UserBalanceRequestDto;
import com.payservice.dto.user.AgeGroupQueryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import static com.payservice.domain.QAccount.account;
import static com.payservice.domain.QAccountDetails.accountDetails;
import static com.payservice.domain.QUser.user;


@Slf4j
@Repository
public class AccountBalanceRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public AccountBalanceRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }


    /**
     * @author 승현
     * @description 나이 연령대에 해당하는 평균 예치금을 조회한다.
     *
     * @param startAge  연령대 시작 나이
     * @param endAge    연령대 끝 나이
     * @param isDeposit 입금 또는 출금 여부
     * @return
     */
    public List<Long> getAccountFlow(int startAge, int endAge, String isDeposit) {
        return queryFactory.select(accountDetails.amount.sum())
                .from(accountDetails)
                .join(accountDetails.account, account)
                .join(account.user, user)
                .where(user.age.between(startAge, endAge)
                        .and(accountDetails.isDeposit.eq(isDeposit))
                )
                .groupBy(accountDetails.account)
                .fetch();
    }


    /**
     * 유저 연령대 및 연령대 별 인원 수를 조회한다.
     *
     * @return
     */
    public List<AgeGroupQueryDto> getAgeGroupList() {
        List<Object[]> resultList = em.createNativeQuery("select FLOOR(us.age/10)*10 as age_group ,COUNT(*) AS total from USER us GROUP by age_group")
                .getResultList();
        return resultList.stream()
                .map(result -> new AgeGroupQueryDto(((BigDecimal) result[0]).intValue(), ((BigInteger) result[1]).intValue()))
                .collect(Collectors.toList());
    }


    /**
     * 한 해 동안 발생한 계좌의 자금 흐름을 조회한다.
     *
     * @param year
     * @param isDeposit
     * @return
     */
    public Long getAccountFlowByYear(Integer year, String isDeposit) {
        return queryFactory.select(accountDetails.amount.sum())
                .from(accountDetails)
                .where(accountDetails.isDeposit.eq(isDeposit)
                        .and(accountDetails.depositDate.year().eq(year))
                )
                .fetchOne();
    }

    public List<UserBalanceDto> getBalance(UserBalanceRequestDto topBalanceUserRequestDto, String isDeposit) {
        return queryFactory.select(
                        new QUserBalanceDto(user.id, user.name, accountDetails.amount.sum())
                )
                .from(accountDetails)
                .join(accountDetails.account, account)
                .join(accountDetails.account.user, user)
                .where(accountDetails.isDeposit.eq(isDeposit)
                        .and(accountDetails.depositDate.between(topBalanceUserRequestDto.getStartDate(), topBalanceUserRequestDto.getEndDate()))
                )
                .groupBy(user.id)
                .orderBy(accountDetails.amount.sum().desc())
                .fetch();
    }
}
