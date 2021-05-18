package com.assignment.income.service;

import com.assignment.income.model.Income;
import com.assignment.income.model.TransactionResponse;
import com.assignment.income.model.UserBalanceResponse;

public interface IncomeService {

    TransactionResponse handleTransaction(Income income);
    UserBalanceResponse getBalance(String accountNumber);
}
