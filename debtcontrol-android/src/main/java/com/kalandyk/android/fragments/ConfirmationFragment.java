package com.kalandyk.android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kalandyk.R;
import com.kalandyk.android.adapters.ConfirmationsArrayAdapter;
import com.kalandyk.android.persistent.DebtDataContainer;
import com.kalandyk.api.model.Confirmation;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.User;
import com.kalandyk.android.listeners.ConfirmationDecisionListener;
import com.kalandyk.android.services.ConfirmationService;

import java.util.List;

/**
 * Created by kamil on 12/18/13.
 */
public class ConfirmationFragment extends AbstractFragment {

    private ConfirmationsArrayAdapter adapter;
    private DebtDataContainer cachedData;

    public ConfirmationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cachedData = getAbstractDebtActivity().getCashedData();
        View confirmationListItemView = inflater.inflate(R.layout.fragment_confirmation_list, container, false);
        adapter = initDebtsArrayAdapter();
        initListView(confirmationListItemView);
        return confirmationListItemView;
    }

    private void initListView(View confirmationListItemView) {
        ListView listView = (ListView) confirmationListItemView.findViewById(R.id.confirmations_list_view);
        listView.setAdapter(adapter);
    }

    private ConfirmationsArrayAdapter initDebtsArrayAdapter() {
        ConfirmationsArrayAdapter confirmationsArrayAdapter =
                new ConfirmationsArrayAdapter(getActivity(), getEnrichedConfirmations());
        confirmationsArrayAdapter.setConfirmationDecisionListener(new ConfirmationDecisionListener() {

            @Override
            public void onAccept(Confirmation confirmation) {
                //TODO: trigger action
                //confirmationService.accept(confirmation);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onReject(Confirmation confirmation) {
                //TODO: trigger action
                //confirmationService.reject(confirmation);
                adapter.notifyDataSetChanged();
            }
        });
        return confirmationsArrayAdapter;
    }

    private List<Confirmation> getEnrichedConfirmations() {
        List<Confirmation> enrichedConfirmations = cachedData.getConfirmations();
        for(Confirmation confirmation : enrichedConfirmations){
            Debt connectedDebt = confirmation.getConnectedDebt();
            connectedDebt = cachedData.enrichDebt(connectedDebt);
            User otherSide = connectedDebt.getDebtor().getId() != cachedData.getLoggedUser().getId() ? connectedDebt.getDebtor() : connectedDebt.getCreditor();
            otherSide = cachedData.enrichUser(otherSide);

            confirmation.setConnectedDebt(connectedDebt);

        }
        return enrichedConfirmations;
    }

    //private class ConfirmationActionTask
}
