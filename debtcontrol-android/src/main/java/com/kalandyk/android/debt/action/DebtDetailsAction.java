package com.kalandyk.android.debt.action;

import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.api.model.Debt;

/**
 * Created by kamil on 2/17/14.
 */
public class DebtDetailsAction extends DebtAction {

    public DebtDetailsAction(AbstractDebtActivity activity) {
        super(activity);
    }

    @Override
    public String getDebtActionButtonText() {
        return "Details";
    }

    @Override
    public void executeAction(Debt debt) {

    }

    @Override
    protected void taskFinished(Debt debt) {

    }
}
