package com.payservice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Entity
public class Account {

    @Id
    private String accountNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Account(String accountNumber, User user) {
        this.accountNumber = accountNumber;
        this.user = user;
    }
}
