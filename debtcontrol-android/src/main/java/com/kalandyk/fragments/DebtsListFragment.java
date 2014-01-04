package com.kalandyk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kalandyk.R;
import com.kalandyk.activities.AbstractActivity;
import com.kalandyk.adapters.DebtsArrayAdapter;
import com.kalandyk.api.model.Debt;
import com.kalandyk.listeners.DebtActionListener;
import com.kalandyk.services.DebtService;

/**
 * Created by kamil on 12/1/13.
 */
public class DebtsListFragment extends Fragment {

    private DebtsArrayAdapter adapter;
    private DebtService debtService;

    public DebtsListFragment() {
        debtService = DebtService.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View debtListItemView = inflater.inflate(R.layout.fragment_debt_list, container, false);

        adapter = initDebtsArrayAdapter();

        initListView(debtListItemView);

        return debtListItemView;
    }

    public void notifyDataChanged() {
        adapter.notifyDataSetChanged();
    }

    protected void showDetailsFragment(Debt debt) {

    }

    private DebtsArrayAdapter initDebtsArrayAdapter() {
        DebtsArrayAdapter debtsArrayAdapter = new DebtsArrayAdapter(getActivity(), debtService.getDebtsForUser(null));


        debtsArrayAdapter.setDebtActionListener(new DebtActionListener() {
            @Override
            public void onDetails(Debt debt) {
                showDetailsFragment(debt);
            }

            @Override
            public void onChangeDebtState(Debt debt) {
                Log.d(AbstractActivity.TAG, " [DebtListFragment] onChangeDebtState() triggered");
                adapter.notifyDataSetChanged();
            }
        });

        return debtsArrayAdapter;
    }

    private void initListView(View inflate) {
        ListView listView = (ListView) inflate.findViewById(R.id.debt_details_list_view);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClickDebtListItemAction(i);
            }
        });
    }

    private void onClickDebtListItemAction(int selectedItem) {
        Debt debtObject = adapter.getDebtObject(selectedItem);
        for (Debt debt : adapter.getObjectList()) {
            debt.setSelected(false);
        }
        debtObject.setSelected(true);
        adapter.notifyDataSetChanged();
    }
}
