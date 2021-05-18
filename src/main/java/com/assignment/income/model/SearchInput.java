package com.assignment.income.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Object that handles the search input the the user sends to get their balance
 */
@Data
public class SearchInput {

    @NotNull
    private String accountNumber;
}
