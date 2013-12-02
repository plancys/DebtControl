package com.kalandyk.fragments;

import android.app.ListFragment;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.kalandyk.adapters.DebtsArrayAdapter;
import com.kalandyk.api.model.Debt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 11/26/13.
 */
public class DebtListFragment extends ListFragment {

    public DebtListFragment(List<Debt> debts){
        this.debts = debts;
    }

    private DebtsArrayAdapter adapter;
    private List<Debt> debts;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Debt debtObject = adapter.getDebtObject(position);
        debtObject.setSelected(!debtObject.isSelected());
        adapter.notifyDataSetChanged();
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new DebtsArrayAdapter(getActivity(), debts );
        setListAdapter(adapter);

    }
}