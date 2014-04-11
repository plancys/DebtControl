package com.kalandyk.exception;

import com.kalandyk.api.model.Debt;

/**
 * DEBT CONTROL
 * Created by Kamil Kalandyk on 3/20/14.
 */
public class DebtControlException extends Exception {

    private String message;
    private ExceptionType exceptionType;

    public DebtControlException(String message) {
        this.message = message;
        exceptionType = ExceptionType.CUSTOM;
    }

    public DebtControlException(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public DebtControlException(ExceptionType exceptionType, String message){
        this.exceptionType = exceptionType;
        this.message = message;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("DebtControlException")
                .append("{Type: ").append(exceptionType)
                .append(", Message: ").append(message).append("]")
                .toString();
    }


}
