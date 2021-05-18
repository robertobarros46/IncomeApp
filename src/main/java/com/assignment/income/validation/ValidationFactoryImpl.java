package com.assignment.income.validation;

import com.assignment.income.validation.transaction.AccountDoesNotExistsValidation;
import com.assignment.income.validation.transaction.InsufficientLimitValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Factory responsible for initializing rules to be validated
 */
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ValidationFactoryImpl implements ValidationFactory {

    private final InsufficientLimitValidation insufficientLimitValidation;
    private final AccountDoesNotExistsValidation accountDoesNotExistsValidation;

    @Override
    public List<Rule> getValidations() {
        return Arrays.asList(insufficientLimitValidation, accountDoesNotExistsValidation);
    }
}
