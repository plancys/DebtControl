package com.kalandyk.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.kalandyk.R;
import com.kalandyk.api.model.Debt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 11/26/13.
 */
public class DebtsArrayAdapter extends ArrayAdapter<Debt> {

    private LayoutInflater layoutInflater;
    private List<Debt> data;
    private Activity activity;


    public DebtsArrayAdapter(Activity context, List<Debt> objects) {
        super(context, R.layout.list_row, objects);
        this.data = objects;
        this.activity = context;
        layoutInflater = context.getLayoutInflater();
    }


    public void setData(List<Debt> data){
        clear();
        if(data == null){
            return;
        }

        for(Debt debt : data){
            add(debt);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.list_row, parent, false);
        } else {
            view = convertView;
        }

        Debt item = getItem(position);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.context_menu);
        if(item.isSelected()){
            linearLayout.setVisibility(View.VISIBLE);
        }else {
            linearLayout.setVisibility(View.GONE);
        }

        return view;
    }

    public Debt getDebtObject(int position){
        return getItem(position);
    }
}
