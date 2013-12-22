package com.kalandyk.debt.action;

import android.app.Activity;

import com.kalandyk.api.model.Debt;

/**
 * Created by kamil on 12/22/13.
 */
public abstract class DebtAction {

    protected Activity activity;

    public DebtAction(Activity activity) {
        this.activity = activity;
    }

    public abstract String getDebtActionString();

    public abstract void executeAction(Debt debt);

}
