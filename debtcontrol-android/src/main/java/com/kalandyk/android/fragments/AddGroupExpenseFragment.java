package com.kalandyk.android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kalandyk.R;
import com.kalandyk.android.adapters.AbstractArrayAdapter;

/**
 * Created by kamil on 12/1/13.
 */
public class AddGroupExpenseFragment extends AbstractFragment {

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container) {
        View debtListItemView = inflater.inflate(R.layout.fragment_group_expense, container, false);
        return debtListItemView;
    }

    @Override
    public AbstractArrayAdapter getFragmentArrayAdapter() {
        return null;
    }
}