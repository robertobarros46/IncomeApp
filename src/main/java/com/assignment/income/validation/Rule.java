package com.assignment.income.validation;

import com.assignment.income.model.Income;

public interface Rule {

    /**
     * Validates transaction checking for violations
     * @param transaction Transaction received on endpoint
     * @return violation
     */
    String validate(Income transaction);

}
