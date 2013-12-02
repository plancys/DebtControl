package com.kalandyk.api.model;

/**
 * Created by kamil on 12/2/13.
 */
public enum DebtState {
    // Debt with confirmation
    /* */
    NOT_CONFIRMED_DEBT,
    /* */
    NOT_PAYED_OFF_CONFIRMED_DEBT,
    /* */
    NOT_CONFIRMED_PAY_OFF_DEBT,
    /* */
    CONFIRMED_PAY_OFF_DEBT,

    // Debt without confirmation
    UNPAID_DEBT,
    PAYED_OFF_DEBT,

    ARCHIVED,
    DELETED

}
