package com.kalandyk.android.debt.action;

import android.app.Activity;
import android.util.Log;

import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractActivity;
import com.kalandyk.api.model.Debt;

/**
 * Created by kamil on 12/22/13.
 */

//Action possible when debt is not confirmed or it is Debt without confirmation
public class DebtDeleteAction extends DebtAction {

    public DebtDeleteAction(Activity activity) {
        super(activity);
    }

    @Override
    public String getDebtActionString() {
        return activity.getString(R.string.debt_delete);
    }

    @Override
    public void executeAction(Debt debt) {
        Log.d(AbstractActivity.TAG, "[DebtAction] Triggered debt delete");
        debtService.deleteDebt(debt);
    }
}
