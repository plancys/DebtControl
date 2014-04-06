package com.kalandyk.android.debt.action;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.adapters.AbstractArrayAdapter;
import com.kalandyk.android.fragments.AbstractFragment;
import com.kalandyk.android.task.AbstractDebtTask;
import com.kalandyk.api.model.Debt;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * Created by kamil on 12/22/13.
 */
public class DebtPayOffActionWithConfirmation extends DebtAction {

    public DebtPayOffActionWithConfirmation(AbstractDebtActivity activity) {
        super(activity);
    }

    @Override
    public String getDebtActionButtonText() {
        return activity.getString(R.string.debt_pay_off);
    }

    @Override
    public void executeAction(Debt debt) {
        Log.d(AbstractDebtActivity.TAG, "[DebtAction] Triggered debt pay off action");
        //TODO: Delegate this action to service or sth like that
        //debtService.requestDebtPayOff(debt);
        progressDialog.show();
        //requestDebtRepayingTask(debt);
        new RequestRepayDebtTask().execute(debt);
    }

    @Override
    protected void taskFinished(Debt debt) {
        if(debtActionListener != null){
            debtActionListener.onChangeDebtState(debt);
        }
        progressDialog.dismiss();
    }

    private class RequestRepayDebtTask extends AbstractDebtTask<Debt, Void, Debt> {

        @Override
        protected AbstractDebtActivity getDebtActivity() {
            return activity;
        }

        @Override
        protected Debt doInBackground(Debt... debts) {
            Debt debt = debts[0];
            try {
//                debt = restTemplate.postForObject(urls.getRequestDebtRepayingUrl(), debt, Debt.class);
                ResponseEntity<Debt> responseDebt = restTemplate
                        .exchange(urls.getRequestDebtRepayingUrl(), HttpMethod.POST, getAuthHeaders(), Debt.class, debt);
                debt = responseDebt.getBody();
            }catch (Exception e){
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
