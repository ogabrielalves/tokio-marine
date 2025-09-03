package com.tokio.marine.demo.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransferRequest {
    @NotBlank(message = "Source account is required")
    private String sourceAccount;

    @NotBlank(message = "Destination account is required")
    private String destinationAccount;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Transfer date is required")
    @FutureOrPresent(message = "Transfer date cannot be in the past")
    private LocalDate transferDate;
}
