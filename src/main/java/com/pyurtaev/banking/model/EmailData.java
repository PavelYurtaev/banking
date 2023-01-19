package com.pyurtaev.banking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EmailData {

    @Id
    @SequenceGenerator(name = "email_data_seq", sequenceName = "email_data_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_data_seq")
    @Column(unique = true, updatable = false, nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @NotNull
    @Size(max = 200)
    @Column(length = 200, unique = true, nullable = false)
    @Email(regexp = "^(.+)@(\\S+)$") // the same at dto
    private String email;
}
