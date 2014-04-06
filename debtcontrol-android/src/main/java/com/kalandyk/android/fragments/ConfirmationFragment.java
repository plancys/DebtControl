package com.kalandyk.android.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.adapters.AbstractArrayAdapter;
import com.kalandyk.android.adapters.ConfirmationsArrayAdapter;
import com.kalandyk.android.persistent.DebtDataContainer;
import com.kalandyk.android.task.AbstractDebtTask;
import com.kalandyk.api.model.Confirmation;
import com.kalandyk.android.listeners.ConfirmationDecisionListener;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.wrapers.ConfirmationDecision;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * Created by kamil on 12/18/13.
 */
public class ConfirmationFragment extends AbstractFragment {

    private ConfirmationsArrayAdapter adapter;
    private ProgressDialog progressDialog;

    public ConfirmationFragment() {
    }

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container) {
        View confirmationListItemView = inflater.inflate(R.layout.fragment_confirmation_list, container, false);
        adapter = initDebtsArrayAdapter();
        initListView(confirmationListItemView);
        progressDialog = getAbstractDebtActivity().getProgressDialog("Sending decision");
        return confirmationListItemView;
    }

    private void initListView(View confirmationListItemView) {
        ListView listView = (ListView) confirmationListItemView.findViewById(R.id.confirmations_list_view);
        listView.setAdapter(adapter);
    }

    private ConfirmationsArrayAdapter initDebtsArrayAdapter() {
        ConfirmationsArrayAdapter confirmationsArrayAdapter =
                new ConfirmationsArrayAdapter(getAbstractDebtActivity(), cachedData.getConfirmations());
        confirmationsArrayAdapter.setConfirmationDecisionListener(new ConfirmationDecisionListener() {

            @Override
            public void onAccept(Confirmation confirmation) {
                //TODO: trigger action
                //confirmationService.accept(confirmation);
                progressDialog.show();
                sendPreparedConfirmationDecision(confirmation, true);
            }

            @Override
            public void onReject(Confirmation confirmation) {
                //TODO: trigger action
                //confirmationService.reject(confirmation);
                progressDialog.show();
                sendPreparedConfirmationDecision(confirmation, false);
            }

            private void sendPreparedConfirmationDecision(Confirmation confirmation, Boolean decision) {
                ConfirmationDecision confirmationDecision = new ConfirmationDecision();
                confirmationDecision.setConfirmation(confirmation);
                confirmationDecision.setDecision(decision);
                new SendConfirmationDecisionTask().execute(confirmationDecision);
            }
        });
        return confirmationsArrayAdapter;
    }

    @Override
    public AbstractArrayAdapter getFragmentArrayAdapter() {
       return adapter;
    }

    private class SendConfirmationDecisionTask extends AbstractDebtTask<ConfirmationDecision, Void, Confirmation> {
        @Override
        protected Confirmation doInBackground(ConfirmationDecision... confirmationDecisions) {
            Debt debt = null;
            try {
//                Boolean decisionMade = restTemplate.postForObject(urls.getSendConfirmationDecisionUrl(), confirmationDecisions[0], Boolean.class);
                ResponseEntity<Debt> responseDebt = restTemplate
                        .exchange(urls.getSendConfirmationDecisionUrl(), HttpMethod.POST, getAuthHeaders(), Debt.class, confirmationDecisions[0]);
                debt = responseDebt.getBody();
                if (debt != null) {
                    //TODO: do it another way - replace debt/remove confirmation
                    loadDebtsToCacheTask();
                    loadConfirmationsToCacheTask();
                }
                return confirmationDecisions[0].getConfirmation();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected AbstractDebtActivity getDebtActivity() {
            return getAbstractDebtActivity();
        }

        @Override
        protected void onPostExecute(Confirmation confirmation) {
            Log.d(AbstractDebtActivity.TAG, "onPostExcecute");
            progressDialog.dismiss();
            adapter.refreshDataInList();
        }
    }
}
