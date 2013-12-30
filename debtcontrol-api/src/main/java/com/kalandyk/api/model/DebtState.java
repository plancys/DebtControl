package com.kalandyk.api.model;

/**
 * Created by kamil on 12/2/13.
 */
public enum DebtState {
    // Debt with confirmation
    /* */
    NOT_CONFIRMED_DEBT(0),
    /* */
    CONFIRMED_NOT_REPAID_DEBT(1),
    /* */
    CONFIRMED_DEBT_WITH_NO_CONFIRMED_REPAYMENT(2),
    /* */
    CONFIRMED_REPAID_DEBT(3),

    // Debt without confirmation
    UNPAID_DEBT(4),
    PAYED_OFF_DEBT(5),

    ARCHIVED(7),
    DELETED(8);

    private int code;

    private DebtState(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }

}
