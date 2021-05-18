package com.assignment.income.controller;

import com.assignment.income.model.Income;
import com.assignment.income.model.SearchInput;
import com.assignment.income.model.TransactionResponse;
import com.assignment.income.model.UserBalanceResponse;
import com.assignment.income.service.IncomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@Api(value = "Income")
@RequestMapping("/user")
public class IncomeController {

    private final IncomeService incomeService;

    @Autowired
    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    /**
     * Transaction endpoint.
     *
     * @param transaction JSON-based data input
     * @return HTTP Response: 201 - Created.
     */
    @ApiOperation(value = "Makes a transaction on user's account")
    @PostMapping("/account")
    public ResponseEntity<TransactionResponse> makeTransaction(@Valid @RequestBody Income transaction) {
        log.info("[IncomeController] - Request made to transaction endpoint");
        final TransactionResponse transactionResponse = incomeService.handleTransaction(transaction);
        return ResponseEntity.status(transactionResponse.getStatus()).body(transactionResponse);
    }

    /**
     * Get user's current balance endpoint.
     *
     * @param userBalance JSON-based data input.
     * @return HTTP Response: 200 - with the account details.
     */
    @ApiOperation(value = "Get user's current balance")
    @PostMapping("/account/search")
    public ResponseEntity<UserBalanceResponse> getUsersBalance(@Valid @RequestBody SearchInput userBalance) {
        log.info("[IncomeController] - Request made to get users balance endpoint");
        final UserBalanceResponse userBalanceResponse = incomeService.getBalance(userBalance.getAccountNumber());
        return ResponseEntity.status(userBalanceResponse.getStatus()).body(userBalanceResponse);
    }
}
