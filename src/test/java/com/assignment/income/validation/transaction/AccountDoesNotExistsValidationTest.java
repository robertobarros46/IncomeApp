package com.assignment.income.validation.transaction;

import com.assignment.income.model.Income;
import com.assignment.income.model.TransactionType;
import com.assignment.income.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountDoesNotExistsValidationTest {

    @Autowired
    private AccountDoesNotExistsValidation accountDoesNotExistsValidation;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    public void testGetBalance() {
        final Income income = Income.builder()
                                    .accountNumber("123")
                                    .amount(100.0)
                                    .transactionType(TransactionType.ADD)
                                    .build();


        when(accountRepository.getUserBalance(anyString())).thenReturn(100.0);

        final String validation = accountDoesNotExistsValidation.validate(income);
        assertTrue(validation.isEmpty());
    }

    @Test
    public void testGetBalanceAccountNotFound() {
        final Income income = Income.builder()
                                    .accountNumber("123")
                                    .amount(100.0)
                                    .transactionType(TransactionType.ADD)
                                    .build();


        when(accountRepository.getUserBalance(anyString())).thenReturn(null);

        final String validation = accountDoesNotExistsValidation.validate(income);
        assertEquals(validation, "account-does-not-exists");
    }
}