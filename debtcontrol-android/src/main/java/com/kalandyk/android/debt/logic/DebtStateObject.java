package com.kalandyk.android.debt.logic;

import com.kalandyk.api.model.DebtState;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtPosition;
import com.kalandyk.api.model.DebtType;
import com.kalandyk.android.debt.action.DebtAction;
import com.kalandyk.android.debt.action.DebtCancelPaidOfRequestAction;
import com.kalandyk.android.debt.action.DebtDeleteAction;
import com.kalandyk.android.debt.action.DebtPayOffActionWithConfirmation;
import com.kalandyk.android.debt.action.DebtPayOffActionWithoutConfirmation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 12/22/13.
 */
public class DebtStateObject {


    private AbstractDebtActivity activity;
    private Debt debt;

    public DebtStateObject(AbstractDebtActivity activity, Debt debt) {
        this.activity = activity;
        this.debt = debt;
    }

    public DebtState getState() {
        return debt.getDebtState();
    }

    public List<DebtAction> getPossibleDebtActions() {
        List<DebtAction> debtActions = new ArrayList<DebtAction>();
        if(debt.getDebtPosition().equals(DebtPosition.CREDITOR)){
            return debtActions;
        }
        DebtType debtType = debt.getDebtType();
        if (debtType.equals(DebtType.DEBT_WITHOUT_CONFIRMATION)) {
            prepareUnconfirmedDebtActions(debtActions);
        } else if (debtType.equals(DebtType.DEBT_WITH_CONFIRMATION)) {
           prepareConfirmedDebtActions(debtActions);
        }
        return debtActions;
    }

    private void prepareUnconfirmedDebtActions(List<DebtAction> debtActions) {
        DebtState debtState = debt.getDebtState();
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
        DebtState debtState = debt.getDebtState();
        switch (debtState){
            case NOT_CONFIRMED_DEBT:
                debtActions.add(new DebtDeleteAction(activity));
                break;
            case CONFIRMED_NOT_REPAID_DEBT:
                debtActions.add(new DebtPayOffActionWithConfirmation(activity));
                break;
            case CONFIRMED_DEBT_WITH_NO_CONFIRMED_REPAYMENT:
                debtActions.add(new DebtCancelPaidOfRequestAction(activity));
                break;
            case CONFIRMED_REPAID_DEBT:
                //TODO: This may be useless
                debtActions.add(new DebtDeleteAction(activity));
                break;
        }
    }


}
