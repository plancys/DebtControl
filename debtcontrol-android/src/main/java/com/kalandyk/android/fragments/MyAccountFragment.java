package com.kalandyk.android.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kalandyk.R;
import com.kalandyk.android.adapters.AbstractArrayAdapter;

public class MyAccountFragment extends AbstractFragment {
    private TextView loginTextView;

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        loginTextView = (TextView) view.findViewById(R.id.tv_my_account_login);
        return view;
    }

    @Override
    public AbstractArrayAdapter getFragmentArrayAdapter() {
        return null;
    }
}
