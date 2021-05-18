package com.assignment.income.controller;

import com.assignment.income.model.TransactionResponse;
import com.assignment.income.model.UserBalanceResponse;
import com.assignment.income.service.IncomeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = IncomeController.class)
class IncomeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IncomeService incomeService;

    @Test
    public void testGetUserBalance() throws Exception {
        final UserBalanceResponse expectedUserBalance = UserBalanceResponse.builder()
                                                                           .accountBalance(100.0)
                                                                           .status(HttpStatus.OK)
                                                                           .build();
        when(incomeService.getBalance("123")).thenReturn(expectedUserBalance);

        this.mvc.perform(post("/user/account/search").contentType(MediaType.APPLICATION_JSON)
            .content("{\"accountNumber\": \"123\"}"))
            .andExpect(status().isOk())
            .andExpect(content().string("{\"status\":\"OK\",\"accountBalance\":100.0}"));
    }

    @Test
    public void testGetUserBalanceAccountNotExists() throws Exception {
        final UserBalanceResponse expectedUserBalance = UserBalanceResponse.builder()
                                                                           .accountBalance(100.0)
                                                                           .status(HttpStatus.NOT_FOUND)
                                                                           .errorMessage("Account not found")
                                                                           .build();
        when(incomeService.getBalance("123")).thenReturn(expectedUserBalance);

        this.mvc.perform(post("/user/account/search").contentType(MediaType.APPLICATION_JSON)
             .content("{\"accountNumber\": \"123\"}"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("{\"status\":\"NOT_FOUND\","
                    + "\"accountBalance\":100.0,\"errorMessage\":\"Account not found\"}"));
    }

    @Test
    public void testMakeTransaction() throws Exception {
        final TransactionResponse expectedTransactionResponse = TransactionResponse.builder()
                                                                           .status(HttpStatus.CREATED)
                                                                           .build();
        when(incomeService.handleTransaction(any())).thenReturn(expectedTransactionResponse);

        this.mvc.perform(post("/user/account").contentType(MediaType.APPLICATION_JSON)
            .content("{\"accountNumber\":\"123\",\"amount\":100.0, \"transactionType\":\"ADD\"}"))
            .andExpect(status().isCreated())
            .andExpect(content().string("{\"status\":\"CREATED\"}"));
    }

    @Test
    public void testMakeTransactionValidationError() throws Exception {
        List<String> validations = new ArrayList<>();
        validations.add("insufficient-limit");
        final TransactionResponse expectedTransactionResponse = TransactionResponse.builder()
                                                                                   .status(HttpStatus.BAD_REQUEST)
                                                                                   .validationErrors(validations)
                                                                                   .build();
        when(incomeService.handleTransaction(any())).thenReturn(expectedTransactionResponse);

        this.mvc.perform(post("/user/account").contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountNumber\":\"123\",\"amount\":100.0, \"transactionType\":\"ADD\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"status\":\"BAD_REQUEST\","
                        + "\"validationErrors\":[\"insufficient-limit\"]}"));
    }
}
