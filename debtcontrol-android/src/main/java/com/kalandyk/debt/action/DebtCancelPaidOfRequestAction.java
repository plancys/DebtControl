package com.kalandyk.debt.action;

import android.app.Activity;
import android.util.Log;

import com.kalandyk.R;
import com.kalandyk.activities.AbstractActivity;
import com.kalandyk.api.model.Debt;

/**
 * Created by kamil on 12/22/13.
 */

//Action possible in Debt with confirmation when User request Debt paid off but other side not confirmed it
public class DebtCancelPaidOfRequestAction extends DebtAction {

    public DebtCancelPaidOfRequestAction(Activity activity) {
        super(activity);
    }

    @Override
    public String getDebtActionString() {
        return activity.getString(R.string.debt_cancel_pay_off_request);
    }

    @Override
    public void executeAction(Debt debt) {
        Log.d(AbstractActivity.TAG, "[DebtAction] Triggered debt cancel paid off request");
        debtService.cancelDebtRepayRequest(debt);
    }
}
