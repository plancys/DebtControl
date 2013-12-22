package com.kalandyk.fragments;

import android.app.Fragment;

import com.kalandyk.api.model.Debt;

/**
 * Created by kamil on 12/22/13.
 */
public class DetailsFragment extends Fragment {

    private Debt debt;

    public DetailsFragment(Debt debt){
        this.debt = debt;
    }

}
