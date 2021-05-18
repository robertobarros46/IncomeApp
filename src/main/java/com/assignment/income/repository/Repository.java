package com.assignment.income.repository;

import com.assignment.income.model.Income;

public interface Repository {

    void addTransaction(String account, Income income);

    Double getUserBalance(String account);

    void deleteIncome(String accountNumber);
}
