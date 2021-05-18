package com.assignment.income.model;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserBalanceResponseTest {

    @Test
    public void testBuilder() {
        final HttpStatus expectedHttpStatus = HttpStatus.ACCEPTED;
        final Double expectedBalance = 100.0;

        UserBalanceResponse userBalanceResponse = UserBalanceResponse.builder()
                                                                     .status(expectedHttpStatus)
                                                                     .accountBalance(expectedBalance)
                                                                     .build();

        assertEquals(userBalanceResponse.getStatus(), expectedHttpStatus);
        assertEquals(userBalanceResponse.getAccountBalance(), expectedBalance);
        assertNull(userBalanceResponse.getErrorMessage());
    }
}
