package com.pyurtaev.banking.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransferDto {

    @NotNull
    private Long userIdTransferTo;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @DecimalMin(value = "0.01")
    private BigDecimal value;
}
