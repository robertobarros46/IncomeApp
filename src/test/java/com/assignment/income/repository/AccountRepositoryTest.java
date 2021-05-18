package com.assignment.income.repository;

import com.assignment.income.model.Income;
import com.assignment.income.model.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    private Repository repository;

    @Test
    public void testRepository() {
        //Given
        final Income income = Income.builder()
                              .accountNumber("123")
                              .amount(100.0)
                              .transactionType(TransactionType.ADD)
                              .build();

        // When
        repository.addTransaction("123456", income);
        final Double userBalance = repository.getUserBalance("123");

        // Then
        assertEquals(income.getAmount(), userBalance);
    }

    @Test
    public void testGetRepositoryAccountDoesNotExists() {
        // When
        final Double userBalance = repository.getUserBalance("123");

        // Then
        assertNull(userBalance);
    }
}