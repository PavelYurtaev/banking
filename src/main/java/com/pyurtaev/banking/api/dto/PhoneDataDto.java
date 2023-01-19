package com.pyurtaev.banking.api.dto;

import com.pyurtaev.banking.validation.RusPhoneNumberFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDataDto {

    private Long id;

    @RusPhoneNumberFormat
    @Size(max = 13)
    private String phone;
}
