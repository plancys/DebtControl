package com.kalandyk.android.debt.action;

import android.util.Log;

import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.api.model.Debt;

/**
 * Created by kamil on 12/22/13.
 */

//Action possible when debt is not confirmed or it is Debt without confirmation
public class DebtDeleteAction extends DebtAction {

    public DebtDeleteAction(AbstractDebtActivity activity) {
        super(activity);
    }

    @Override
    public String getDebtActionString() {
        return activity.getString(R.string.debt_delete);
    }

    @Override
    public void executeAction(Debt debt) {
        Log.d(AbstractDebtActivity.TAG, "[DebtAction] Triggered debt delete");
        activity.getProgressDialog(null).show();
        deleteDebtTask(debt);
    }

    @Override
    protected void taskFinished(Debt debt) {

    }
}
