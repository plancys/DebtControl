package com.kalandyk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kalandyk.R;
import com.kalandyk.adapters.DetailsArrayAdapter;
import com.kalandyk.api.model.Debt;

/**
 * Created by kamil on 12/22/13.
 */
public class DetailsFragment extends Fragment {

    private Debt debt;
    private DetailsArrayAdapter detailsArrayAdapter;
    private ListView listView;

    public DetailsFragment(Debt debt){
        this.debt = debt;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
        detailsArrayAdapter = new DetailsArrayAdapter(getActivity(),debt.getDebtEvents() );
        return detailsArrayAdapter;
    }


}
