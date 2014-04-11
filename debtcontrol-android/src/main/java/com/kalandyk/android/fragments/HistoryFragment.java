package com.kalandyk.android.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kalandyk.R;
import com.kalandyk.android.adapters.AbstractArrayAdapter;

public class HistoryFragment extends AbstractFragment {

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container) {
        View debtListItemView = inflater.inflate(R.layout.fragment_history, container, false);
        return debtListItemView;
    }

    @Override
    public AbstractArrayAdapter getFragmentArrayAdapter() {
        return null;
    }
}
