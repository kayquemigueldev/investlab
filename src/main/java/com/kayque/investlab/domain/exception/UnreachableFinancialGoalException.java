package com.kayque.investlab.domain.exception;

public class UnreachableFinancialGoalException
        extends RuntimeException {

    public UnreachableFinancialGoalException(String message) {
        super(message);
    }
}