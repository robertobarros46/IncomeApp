package com.assignment.income.service;

import com.assignment.income.model.Income;
import com.assignment.income.model.TransactionResponse;
import com.assignment.income.model.TransactionType;
import com.assignment.income.model.UserBalanceResponse;
import com.assignment.income.repository.AccountRepository;
import com.assignment.income.validation.ValidationFactory;
import com.assignment.income.validation.transaction.AccountDoesNotExistsValidation;
import com.assignment.income.validation.transaction.InsufficientLimitValidation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class IncomeServiceImplTest {

    @Autowired
    private IncomeService incomeService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private ValidationFactory validationFactory;

    @MockBean
    private InsufficientLimitValidation insufficientLimitValidation;

    @MockBean
    private AccountDoesNotExistsValidation accountDoesNotExistsValidation;

    @Test
    public void testHandleTransaction() {
        final Income income = Income.builder()
                                    .accountNumber("123")
                                    .amount(100.0)
                                    .transactionType(TransactionType.ADD)
                                    .build();

        doNothing().when(accountRepository).addTransaction(any(), any());

        final TransactionResponse transactionResponse = incomeService.handleTransaction(income);
        assertEquals(transactionResponse.getStatus(), HttpStatus.CREATED);
        assertNull(transactionResponse.getValidationErrors());
    }

    @Test
    public void testHandleTransactionAccountDoesNotExists() {
        final Income income = Income.builder()
                                    .accountNumber("123")
                                    .amount(100.0)
                                    .transactionType(TransactionType.REMOVE)
                                    .build();

        when(validationFactory.getValidations()).thenReturn(Arrays.asList(insufficientLimitValidation,
                accountDoesNotExistsValidation));
        when(accountDoesNotExistsValidation.validate(any())).thenReturn("account-does-not-exists");
        when(insufficientLimitValidation.validate(any())).thenReturn("");

        final TransactionResponse transactionResponse = incomeService.handleTransaction(income);
        assertEquals(transactionResponse.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(transactionResponse.getValidationErrors().get(0), "account-does-not-exists");
    }

    @Test
    public void testHandleTransactionInsufficientLimit() {
        final Income income = Income.builder()
                                    .accountNumber("123")
                                    .amount(100.0)
                                    .transactionType(TransactionType.REMOVE)
                                    .build();

        when(validationFactory.getValidations()).thenReturn(Arrays.asList(insufficientLimitValidation,
                accountDoesNotExistsValidation));
        when(accountDoesNotExistsValidation.validate(any())).thenReturn("");
        when(insufficientLimitValidation.validate(any())).thenReturn("insufficient-limit");

        final TransactionResponse transactionResponse = incomeService.handleTransaction(income);
        assertEquals(transactionResponse.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(transactionResponse.getValidationErrors().get(0), "insufficient-limit");
    }

    @Test
    public void testSuccessfulRemoveTransaction() {
        final Income income = Income.builder()
                                    .accountNumber("123")
                                    .amount(100.0)
                                    .transactionType(TransactionType.REMOVE)
                                    .build();

        when(validationFactory.getValidations()).thenReturn(Arrays.asList(insufficientLimitValidation,
                accountDoesNotExistsValidation));
        when(accountDoesNotExistsValidation.validate(any())).thenReturn("");
        when(insufficientLimitValidation.validate(any())).thenReturn("");

        final TransactionResponse transactionResponse = incomeService.handleTransaction(income);
        assertEquals(transactionResponse.getStatus(), HttpStatus.CREATED);
        assertNull(transactionResponse.getValidationErrors());
    }

    @Test
    public void testGetBalance() {
        final String accountNumber = "123";

        when(accountRepository.getUserBalance(anyString())).thenReturn(100.0);

        final UserBalanceResponse userBalanceResponse = incomeService.getBalance(accountNumber);
        assertEquals(userBalanceResponse.getStatus(), HttpStatus.OK);
        assertNull(userBalanceResponse.getErrorMessage());
    }

    @Test
    public void testGetBalanceAccountNotFound() {
        final String accountNumber = "123";

        when(accountRepository.getUserBalance(anyString())).thenReturn(null);

        final UserBalanceResponse userBalanceResponse = incomeService.getBalance(accountNumber);
        assertEquals(userBalanceResponse.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(userBalanceResponse.getErrorMessage(), "Account not found");
    }
}
