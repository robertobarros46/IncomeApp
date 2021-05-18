package com.assignment.income.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Object that handles the response that user gets when getting the current balance
 */
@Data
@Builder
public class UserBalanceResponse {

    private HttpStatus status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double accountBalance;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorMessage;
}
