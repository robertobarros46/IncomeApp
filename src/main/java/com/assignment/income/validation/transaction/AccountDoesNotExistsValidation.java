package com.assignment.income.validation.transaction;

import com.assignment.income.model.Income;
import com.assignment.income.repository.Repository;
import com.assignment.income.validation.Rule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Rule to validate if a account exists before doing removing transaction or getting users current balance
 */
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class AccountDoesNotExistsValidation implements Rule {

    private final Repository repository;

    @Override
    public String validate(Income transaction) {
        final Double balance = repository.getUserBalance(transaction.getAccountNumber());
        if (balance == null) {
            return "account-does-not-exists";
        }
        return "";
    }
}
