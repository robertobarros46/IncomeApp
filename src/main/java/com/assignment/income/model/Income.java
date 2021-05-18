package com.assignment.income.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Object that holds the Income transaction done by the user on a respective account.
 */
@Data
@Builder
public class Income {

    @NotNull
    private String accountNumber;
    @NotNull
    private Double amount;
    @NotNull
    @ApiModelProperty
    private TransactionType transactionType;
}
