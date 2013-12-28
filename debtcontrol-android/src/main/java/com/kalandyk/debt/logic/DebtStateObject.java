package com.kalandyk.debt.logic;

import android.app.Activity;

import com.kalandyk.api.model.DebtState;
import com.kalandyk.api.model.DebtType;
import com.kalandyk.debt.action.DebtAction;
import com.kalandyk.debt.action.DebtCancelPaidOfRequestAction;
import com.kalandyk.debt.action.DebtDeleteAction;
import com.kalandyk.debt.action.DebtPayOffActionWithConfirmation;
import com.kalandyk.debt.action.DebtPayOffActionWithoutConfirmation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 12/22/13.
 */
public class DebtStateObject {

    private final DebtType debtType;

    private DebtState debtState;

    private Activity activity;

    public DebtStateObject(Activity activity, DebtType debtType, DebtState debtState) {
        this.activity = activity;
        this.debtType = debtType;
        this.debtState = debtState;
    }

    public DebtState getState() {
        return debtState;
    }

    public List<DebtAction> getPossibleDebtActions() {
        List<DebtAction> debtActions = new ArrayList<DebtAction>();
        if (debtType.equals(DebtType.DEBT_WITHOUT_CONFIRMATION)) {
            prepareUnconfirmedDebtActions(debtActions);
        } else if (debtType.equals(DebtType.DEBT_WITH_CONFIRMATION)) {
           prepareConfirmedDebtActions(debtActions);
        }
        return debtActions;
    }

    private void prepareUnconfirmedDebtActions(List<DebtAction> debtActions) {
        switch (debtState){
            case UNPAID_DEBT:
                debtActions.add(new DebtDeleteAction(activity));
                debtActions.add(new DebtPayOffActionWithoutConfirmation(activity));
                break;
            case PAYED_OFF_DEBT:
                //TODO: figure out whether this is useful or not
                debtActions.add(new DebtDeleteAction(activity));
                break;
        }
    }

    private void prepareConfirmedDebtActions(List<DebtAction> debtActions) {
        switch (debtState){
            case NOT_CONFIRMED_DEBT:
                debtActions.add(new DebtDeleteAction(activity));
                break;
            case NOT_PAYED_OFF_CONFIRMED_DEBT:
                debtActions.add(new DebtPayOffActionWithConfirmation(activity));
                break;
            case NOT_CONFIRMED_PAY_OFF_DEBT:
                debtActions.add(new DebtCancelPaidOfRequestAction(activity));
                break;
            case CONFIRMED_PAY_OFF_DEBT:
                //TODO: This may be useless
                debtActions.add(new DebtDeleteAction(activity));
                break;
        }
    }


}
