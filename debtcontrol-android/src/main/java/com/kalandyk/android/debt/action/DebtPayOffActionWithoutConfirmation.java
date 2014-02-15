package com.kalandyk.android.debt.action;

import android.util.Log;

import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtState;

/**
 * Created by kamil on 12/22/13.
 */
public class DebtPayOffActionWithoutConfirmation extends DebtAction {

    public DebtPayOffActionWithoutConfirmation(AbstractDebtActivity activity) {
        super(activity);
    }

    @Override
    public String getDebtActionButtonText() {
        return activity.getString(R.string.debt_pay_off);
    }

    @Override
    public void executeAction(Debt debt) {
        Log.d(AbstractDebtActivity.TAG, "[DebtAction] Triggered debt pay off action");
        debt.setDebtState(DebtState.PAYED_OFF_DEBT);
    }

    @Override
    protected void taskFinished(Debt debt) {

    }
}
