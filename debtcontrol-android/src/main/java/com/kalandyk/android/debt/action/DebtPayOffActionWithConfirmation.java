package com.kalandyk.android.debt.action;

import android.app.Activity;
import android.util.Log;

import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractActivity;
import com.kalandyk.api.model.Debt;

/**
 * Created by kamil on 12/22/13.
 */
public class DebtPayOffActionWithConfirmation extends DebtAction {

    public DebtPayOffActionWithConfirmation(Activity activity) {
        super(activity);
    }

    @Override
    public String getDebtActionString() {
        return activity.getString(R.string.debt_pay_off);
    }

    @Override
    public void executeAction(Debt debt) {
        Log.d(AbstractActivity.TAG, "[DebtAction] Triggered debt pay off action");
        //TODO: Delegate this action to service or sth like that
        debtService.requestDebtPayOff(debt);

    }
}
