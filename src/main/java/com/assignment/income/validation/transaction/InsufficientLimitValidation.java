package com.assignment.income.validation.transaction;

import com.assignment.income.model.Income;
import com.assignment.income.repository.Repository;
import com.assignment.income.validation.Rule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Responsible to validate if there is enough limit on the account for current transaction
 */
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class InsufficientLimitValidation implements Rule {

    private final Repository repository;

    @Override
    public String validate(Income transaction) {
        final Double balance = repository.getUserBalance(transaction.getAccountNumber());
        if (balance == null) {
            return "";
        }
        return (balance - transaction.getAmount()) < 0.0 ? "insufficient-limit" : "";
    }
}
