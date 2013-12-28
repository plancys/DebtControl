package com.kalandyk.adapters;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kalandyk.R;
import com.kalandyk.activities.AbstractActivity;
import com.kalandyk.api.model.Confirmation;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtType;
import com.kalandyk.debt.action.DebtAction;
import com.kalandyk.debt.logic.DebtStateObject;
import com.kalandyk.listeners.DebtItemAction;

import java.util.List;

/**
 * Created by kamil on 11/26/13.
 */
public class ConfirmationsArrayAdapter extends ArrayAdapter<Confirmation> {

    private LayoutInflater layoutInflater;

    public ConfirmationsArrayAdapter(Activity context, List<Confirmation> objects) {
        super(context, R.layout.list_row_confirmations, objects);
        layoutInflater = context.getLayoutInflater();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        //TODO: add action when convertView is not empty
        view = layoutInflater.inflate(R.layout.list_row_confirmations, parent, false);

        return view;
    }

}
