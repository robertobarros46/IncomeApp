package com.assignment.income.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Object that handles the response that user gets when making a transaction
 */
@Data
@Builder
public class TransactionResponse {

    private HttpStatus status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> validationErrors;
}
