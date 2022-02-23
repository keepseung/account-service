package com.payservice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class AccountDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number")
    private Account account;

    @Column(length = 2, nullable = false)
    private String isDeposit;

    @Column(nullable = false)
    private Long amount;

    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate depositDate;

    @Builder
    public AccountDetails(Account account, String isDeposit, Long amount, LocalDate depositDate) {
        this.account = account;
        this.isDeposit = isDeposit;
        this.amount = amount;
        this.depositDate = depositDate;
    }
}
