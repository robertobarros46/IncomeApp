package com.assignment.income.service;

import com.assignment.income.model.Income;
import com.assignment.income.model.TransactionResponse;
import com.assignment.income.model.TransactionType;
import com.assignment.income.model.UserBalanceResponse;
import com.assignment.income.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class IncomeServiceImplIT {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private Repository repository;

    @BeforeEach
    public void removeIncome() {
        repository.deleteIncome("123");
    }

    @Test
    public void testHandleTransaction() {
        // Given
        final Income income = Income.builder()
                                    .accountNumber("123")
                                    .amount(100.0)
                                    .transactionType(TransactionType.ADD)
                                    .build();

        // When
        incomeService.handleTransaction(income);
        final UserBalanceResponse userBalanceResponse = incomeService.getBalance(income.getAccountNumber());

        // Then
        assertEquals(userBalanceResponse.getStatus(), HttpStatus.OK);
        assertEquals(userBalanceResponse.getAccountBalance(), 100.0);
        assertNull(userBalanceResponse.getErrorMessage());
    }

    @Test
    public void testHandleTransactionAccountDoesNotExists() {
        // Given
        final Income income = Income.builder()
                                    .accountNumber("123")
                                    .amount(100.0)
                                    .transactionType(TransactionType.REMOVE)
                                    .build();

        //When
        final TransactionResponse transactionResponse = incomeService.handleTransaction(income);

        // Then
        assertEquals(transactionResponse.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(transactionResponse.getValidationErrors().get(0), "account-does-not-exists");
    }

    @Test
    public void testHandleTransactions() {
        // Given
        final Income income = Income.builder()
                                    .accountNumber("123")
                                    .amount(100.0)
                                    .transactionType(TransactionType.ADD)
                                    .build();
        // When
        incomeService.handleTransaction(income);
        incomeService.handleTransaction(income);

        final UserBalanceResponse userBalanceResponse = incomeService.getBalance(income.getAccountNumber());

        // Then
        assertEquals(userBalanceResponse.getStatus(), HttpStatus.OK);
        assertEquals(userBalanceResponse.getAccountBalance(), 200.0);
        assertNull(userBalanceResponse.getErrorMessage());
    }

    @Test
    public void testHandleTransactionThenRemove() {
        // Given
        final Income addIncome = Income.builder()
                                    .accountNumber("123")
                                    .amount(100.0)
                                    .transactionType(TransactionType.ADD)
                                    .build();

        final Income removeIncome = Income.builder()
                                    .accountNumber("123")
                                    .amount(90.0)
                                    .transactionType(TransactionType.REMOVE)
                                    .build();
        // When
        incomeService.handleTransaction(addIncome);
        incomeService.handleTransaction(removeIncome);

        final UserBalanceResponse userBalanceResponse = incomeService.getBalance(addIncome.getAccountNumber());

        // Then
        assertEquals(userBalanceResponse.getStatus(), HttpStatus.OK);
        assertEquals(userBalanceResponse.getAccountBalance(), 10.0);
        assertNull(userBalanceResponse.getErrorMessage());
    }

    @Test
    public void testHandleTransactionWithInsufficientLimits() {
        // Given
        final Income addIncome = Income.builder()
                                       .accountNumber("123")
                                       .amount(100.0)
                                       .transactionType(TransactionType.ADD)
                                       .build();

        final Income removeIncome = Income.builder()
                                          .accountNumber("123")
                                          .amount(110.0)
                                          .transactionType(TransactionType.REMOVE)
                                          .build();
        // When
        incomeService.handleTransaction(addIncome);
        final TransactionResponse transactionResponse = incomeService.handleTransaction(removeIncome);

        // Then
        assertEquals(transactionResponse.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(transactionResponse.getValidationErrors().get(0), "insufficient-limit");
    }

    @Test
    public void testGetBalance() {
        final Income income = Income.builder()
                                       .accountNumber("123")
                                       .amount(100.0)
                                       .transactionType(TransactionType.ADD)
                                       .build();


        repository.addTransaction("123456", income);

        final UserBalanceResponse userBalanceResponse = incomeService.getBalance(income.getAccountNumber());
        assertEquals(userBalanceResponse.getStatus(), HttpStatus.OK);
        assertNull(userBalanceResponse.getErrorMessage());
    }

    @Test
    public void testGetBalanceAccountNotFound() {
        final String accountNumber = "123";

        final UserBalanceResponse userBalanceResponse = incomeService.getBalance(accountNumber);
        assertEquals(userBalanceResponse.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(userBalanceResponse.getErrorMessage(), "Account not found");
    }
}
