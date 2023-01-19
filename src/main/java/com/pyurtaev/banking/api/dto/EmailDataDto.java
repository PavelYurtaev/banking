package com.pyurtaev.banking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDataDto {

    private Long id;

    @Size(max = 200)
    @Email(regexp = "^(.+)@(\\S+)$")
    private String email;
}
