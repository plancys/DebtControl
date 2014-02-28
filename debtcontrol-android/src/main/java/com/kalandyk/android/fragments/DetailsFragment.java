package com.kalandyk.android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kalandyk.R;
import com.kalandyk.android.adapters.AbstractArrayAdapter;
import com.kalandyk.android.adapters.DetailsArrayAdapter;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 12/22/13.
 */
public class DetailsFragment extends AbstractFragment {

    private Debt debt;
    private DetailsArrayAdapter detailsArrayAdapter;
    private ListView listView;


    public DetailsFragment(Debt debt) {
        this.debt = debt;
    }

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container) {
        View debtListItemView = inflater.inflate(R.layout.fragment_details_list, container, false);
        detailsArrayAdapter = initDebtsArrayAdapter();
        initListView(debtListItemView);
        return debtListItemView;
    }

    private void initListView(View debtListItemView) {
        listView = (ListView) debtListItemView.findViewById(R.id.list_view_debt_details);
        listView.setAdapter(detailsArrayAdapter);
    }

    private DetailsArrayAdapter initDebtsArrayAdapter() {
        List<DebtEvent> events = new ArrayList<DebtEvent>();
       //TODO: implement this
        //events.addAll(debt.getEvents());
        detailsArrayAdapter = new DetailsArrayAdapter(getActivity(), events);
        return detailsArrayAdapter;
    }


    @Override
    public AbstractArrayAdapter getFragmentArrayAdapter() {
        return null;
    }
}
