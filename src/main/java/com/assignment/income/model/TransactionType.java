package com.assignment.income.model;

import io.swagger.annotations.ApiModel;

/**
 * Type of the requested transaction
 */
@ApiModel
public enum TransactionType {
    ADD,
    REMOVE,
}
