package com.assignment.income.repository;

import com.assignment.income.model.Income;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Repository to store all the events that have no violations.
 */

@Component
public class AccountRepository implements Repository {

    private final Map<String, Income> map = new LinkedHashMap<>();

    @Override
    public void addTransaction(String id, Income income){
        map.put(id, income);
    }

    @Override
    public Double getUserBalance(String account) {
        final List<Income> incomes = map.values()
                                        .stream()
                                        .filter(value -> account.equals(value.getAccountNumber()))
                                        .collect(Collectors.toList());
        if (incomes.isEmpty()) {
            return null;
        } else {
            return incomes.stream().mapToDouble(Income::getAmount).sum();
        }
    }

    @Override
    public void deleteIncome(String account) {
        map.entrySet().removeIf(entry -> account.equals(entry.getValue().getAccountNumber()));
    }
}
