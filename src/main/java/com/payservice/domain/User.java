package com.payservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate createDate;

    @Builder
    public User(String name, Integer age, LocalDate createDate) {
        this.name = name;
        this.age= age;
        this.createDate = createDate;
    }
}
