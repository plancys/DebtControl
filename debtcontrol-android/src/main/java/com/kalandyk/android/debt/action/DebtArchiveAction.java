package com.kalandyk.android.debt.action;

import android.util.Log;

import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtState;

/**
 * Created by kamil on 12/22/13.
 */

//Action possible when debt is not confirmed or it is Debt without confirmation
public class DebtArchiveAction extends DebtAction {

    public DebtArchiveAction(AbstractDebtActivity activity) {
        super(activity);
    }

    @Override
    public String getDebtActionButtonText() {
        return activity.getString(R.string.archive_delete);
    }

    @Override
    public void executeAction(Debt debt) {
        Log.d(AbstractDebtActivity.TAG, "[DebtAction] Triggered debt delete");
        activity.getProgressDialog(null).show();
        //deleteDebtTask(debt);
        debt.setDebtState(DebtState.ARCHIVED);
        taskFinished(debt);

    }

    @Override
    protected void taskFinished(Debt debt) {
        if(debtActionListener != null){
            debtActionListener.onChangeDebtState(debt);
        }
    }
}
