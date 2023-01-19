package com.pyurtaev.banking.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

    @Id
    @SequenceGenerator(name = "account_seq", sequenceName = "account_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @Column(unique = true, updatable = false, nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @DecimalMin(value = "0.0")
    @Column(precision = 12, scale = 2)
    private BigDecimal balance;
}