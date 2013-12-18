package com.kalandyk.api.model;

/**
 * Created by kamil on 12/2/13.
 */
public enum DebtState {
    // Debt with confirmation
    /* */
    NOT_CONFIRMED_DEBT(0),
    /* */
    NOT_PAYED_OFF_CONFIRMED_DEBT(1),
    /* */
    NOT_CONFIRMED_PAY_OFF_DEBT(2),
    /* */
    CONFIRMED_PAY_OFF_DEBT(3),

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
