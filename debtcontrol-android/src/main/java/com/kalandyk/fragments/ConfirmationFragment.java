package com.kalandyk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kalandyk.R;

/**
 * Created by kamil on 12/18/13.
 */
public class ConfirmationFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_confirmation, container, false);


        return inflate;
    }


}
