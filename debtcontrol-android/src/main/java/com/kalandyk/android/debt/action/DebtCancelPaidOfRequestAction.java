package com.kalandyk.android.debt.action;

import android.util.Log;

import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.task.AbstractDebtTask;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * Created by kamil on 12/22/13.
 */

//Action possible in Debt with confirmation when User request Debt paid off but other side not confirmed it
public class DebtCancelPaidOfRequestAction extends DebtAction {

    public DebtCancelPaidOfRequestAction(AbstractDebtActivity activity) {
        super(activity);
    }

    @Override
    public String getDebtActionButtonText() {
        return activity.getString(R.string.debt_cancel_pay_off_request);
    }

    @Override
    public void executeAction(Debt debt) {
        Log.d(AbstractDebtActivity.TAG, "[DebtAction] Triggered debt cancel paid off request");
        activity.getProgressDialog(null).show();
        new CancelRequestRepayDebtTask().execute(debt);
    }

    @Override
    protected void taskFinished(Debt debt) {
        if (debtActionListener != null) {
            debtActionListener.onChangeDebtState(debt);
        }
        progressDialog.dismiss();
    }

    private class CancelRequestRepayDebtTask extends AbstractDebtTask<Debt, Void, Debt> {

        @Override
        protected AbstractDebtActivity getDebtActivity() {
            return activity;
        }

        @Override
        protected Debt doInBackground(Debt... debts) {
            Debt debt = debts[0];
            try {
//                debt = restTemplate.postForObject(urls.getCancelRepayingRequestUrl(), debt, Debt.class);
                ResponseEntity<Debt> responseDebt = restTemplate
                        .exchange(urls.getCancelRepayingRequestUrl(), HttpMethod.POST, getAuthHeadersWithRequestObject(debt), Debt.class);
                debt = responseDebt.getBody();
            } catch (Exception e) {
                Log.e(AbstractDebtActivity.TAG, e.getMessage(), e);
            }
            return debt;
        }

        @Override
        protected void onPostExecute(Debt result) {
            taskFinished(result);
        }
    }
}
