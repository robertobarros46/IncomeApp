package com.assignment.income.service;

import com.assignment.income.model.Income;
import com.assignment.income.model.TransactionResponse;
import com.assignment.income.model.TransactionResponse.TransactionResponseBuilder;
import com.assignment.income.model.TransactionType;
import com.assignment.income.model.UserBalanceResponse;
import com.assignment.income.repository.Repository;
import com.assignment.income.validation.ValidationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service layer to handle transactions and get user balance
 */
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class IncomeServiceImpl implements IncomeService{

    private final ValidationFactory validationFactory;
    private final Repository repository;

    /**
     * Method to handle the transaction done by the user on their respectives accounts
     * @param income transaction income
     * @return TransactionResponse
     */
    @Override
    public TransactionResponse handleTransaction(Income income) {
        final TransactionResponseBuilder builder = TransactionResponse.builder();
        final TransactionResponse transactionResponse;
        if (TransactionType.ADD.equals(income.getTransactionType())) {
            transactionResponse = saveTransaction(income, builder);
        } else {
            transactionResponse = removeTypeTransaction(income);
        }

        return transactionResponse;
    }

    /**
     * Method that handles transactions that will remove from user balance
     * @param income transaction income
     * @return TransactionResponse
     */
    private TransactionResponse removeTypeTransaction(Income income) {
        final TransactionResponseBuilder builder = TransactionResponse.builder();
        final TransactionResponse transactionResponse;
        final List<String> validations = validationFactory.getValidations()
                                                          .stream()
                                                          .map(rule -> rule.validate(income))
                                                          .filter(value -> !value.isEmpty())
                                                          .collect(Collectors.toList());
        income.setAmount(income.getAmount() * -1);
        if (validations.isEmpty()) {
            transactionResponse = saveTransaction(income, builder);
        } else {
            builder.status(HttpStatus.BAD_REQUEST).validationErrors(validations);
            transactionResponse = builder.build();
        }
        return transactionResponse;
    }

    /**
     * Method that handles saving new transaction
     * @param income transaction income
     * @param builder builder of resposne
     * @return TransactionResponse
     */
    private TransactionResponse saveTransaction(Income income, TransactionResponseBuilder builder) {
        UUID uuid = UUID.randomUUID();
        repository.addTransaction(uuid.toString(), income);
        return builder.status(HttpStatus.CREATED).build();
    }

    /**
     * Method that gets the user current balance
     * @param accountNumber users account number
     * @return UserBalanceResponse
     */
    @Override
    public UserBalanceResponse getBalance(String accountNumber) {
        final Double balance = repository.getUserBalance(accountNumber);
        if (balance == null) {
            return UserBalanceResponse.builder().status(HttpStatus.NOT_FOUND).errorMessage("Account not found").build();
        }
        return UserBalanceResponse.builder().status(HttpStatus.OK).accountBalance(balance).build();
    }
}
