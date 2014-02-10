package com.kalandyk.android.debt.action;

import android.util.Log;

import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.api.model.Debt;

/**
 * Created by kamil on 12/22/13.
 */
public class DebtPayOffActionWithConfirmation extends DebtAction {

    public DebtPayOffActionWithConfirmation(AbstractDebtActivity activity) {
        super(activity);
    }

    @Override
    public String getDebtActionString() {
        return activity.getString(R.string.debt_pay_off);
    }

    @Override
    public void executeAction(Debt debt) {
        Log.d(AbstractDebtActivity.TAG, "[DebtAction] Triggered debt pay off action");
        //TODO: Delegate this action to service or sth like that
        //debtService.requestDebtPayOff(debt);
        activity.getProgressDialog(null).show();
        requestDebtRepayingTask(debt);
    }

    @Override
    protected void taskFinished(Debt debt) {

    }
}
