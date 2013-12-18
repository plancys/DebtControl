package com.kalandyk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kalandyk.R;
import com.kalandyk.adapters.DebtsArrayAdapter;
import com.kalandyk.api.model.Debt;
import com.kalandyk.services.DebtService;

/**
 * Created by kamil on 12/1/13.
 */
public class DebtsListFragment extends Fragment {

    private DebtsArrayAdapter adapter;

    private DebtService debtService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        debtService = DebtService.getInstance();

        View inflate = inflater.inflate(R.layout.fragment_debt_list, container, false);

        ListView listView = (ListView) inflate.findViewById(R.id.debt_list_view);

        adapter = new DebtsArrayAdapter(getActivity(), debtService.getDebtsForUser(null));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClickDebtListItemAction(i);
            }
        });

        return  inflate;
    }

    private void onClickDebtListItemAction(int selectedItem) {
        Debt debtObject = adapter.getDebtObject(selectedItem);
        for(Debt debt : adapter.getObjectList()){
            debt.setSelected(false);
        }
        debtObject.setSelected(true);
        adapter.notifyDataSetChanged();
    }

    public void notifyDataChanged(){
        adapter.notifyDataSetChanged();
    }
}
