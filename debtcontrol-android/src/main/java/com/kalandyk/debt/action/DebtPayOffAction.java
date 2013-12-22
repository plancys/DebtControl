package com.kalandyk.debt.action;

import android.app.Activity;
import android.util.Log;

import com.kalandyk.R;
import com.kalandyk.activities.AbstractActivity;
import com.kalandyk.api.model.Debt;
import com.kalandyk.debt.action.DebtAction;

/**
 * Created by kamil on 12/22/13.
 */
public class DebtPayOffAction extends DebtAction {

    public DebtPayOffAction(Activity activity) {
        super(activity);
    }

    @Override
    public String getDebtActionString() {
        return activity.getString(R.string.debt_pay_off);
    }

    @Override
    public void executeAction(Debt debt) {
        Log.d(AbstractActivity.TAG, "[DebtAction] Triggered debt pay off action");
    }
}
