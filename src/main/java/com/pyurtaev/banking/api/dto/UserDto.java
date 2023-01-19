package com.pyurtaev.banking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private BigDecimal balance;
    private List<EmailDataDto> emailsData;
    private List<PhoneDataDto> phonesData;
}
