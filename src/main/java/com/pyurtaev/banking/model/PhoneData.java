package com.pyurtaev.banking.model;

import com.pyurtaev.banking.validation.RusPhoneNumberFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PhoneData {

    @Id
    @SequenceGenerator(name = "phone_data_seq", sequenceName = "phone_data_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_data_seq")
    @Column(unique = true, updatable = false, nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @RusPhoneNumberFormat
    @Size(max = 13)
    @Column(length = 13, unique = true, nullable = false)
    private String phone;
}
