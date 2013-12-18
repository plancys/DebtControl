package com.kalandyk.adapters;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kalandyk.R;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtType;

import java.util.List;

/**
 * Created by kamil on 11/26/13.
 */
public class DebtsArrayAdapter extends ArrayAdapter<Debt> {

    private LayoutInflater layoutInflater;
    private List<Debt> data;
    private Activity activity;

    private TextView mainInfoTextView;
    private TextView descriptionTextView;
    private TextView dateTextView;
    private LinearLayout debtSurfaceLinearLayout;


    public DebtsArrayAdapter(Activity context, List<Debt> objects) {
        super(context, R.layout.list_row, objects);
        this.data = objects;
        this.activity = context;
        layoutInflater = context.getLayoutInflater();
    }


    public void setData(List<Debt> data) {
        clear();
        if (data == null) {
            return;
        }

        for (Debt debt : data) {
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

        initUIItems(view);

        Debt item = getItem(position);

        mainInfoTextView.setText("You owe " + item.getAmount() + " PLN");
        descriptionTextView.setText(item.getDescription());
        dateTextView.setText(item.getCreationDate().toString());


        setDebtState(view, item);
        //view.invalidate();

        if (item.isSelected()) {
            debtSurfaceLinearLayout.setVisibility(View.VISIBLE);
        } else {
            debtSurfaceLinearLayout.setVisibility(View.GONE);
        }

        return view;
    }

    private void setDebtState(View view, Debt item) {
        if (item.getDebtType().equals(DebtType.DEBT_WITH_CONFIRMATION)) {
            setStylesForDebtStateWithConfirmation(view, item);
        } else if (item.getDebtType().equals(DebtType.DEBT_WITHOUT_CONFIRMATION)) {
            setStyleForDebtWithoutConfirmation(view, item);
        }
    }

    private void setStyleForDebtWithoutConfirmation(View view, Debt item) {
        TextView[] debtStates;
        debtStates = getDebtWithoutConfirmationViews(view);
        for (TextView textView : getDebtWithConfirmationViews(view)) {
            textView.setVisibility(View.GONE);
        }
        for (TextView textView : debtStates) {
            setStateInactiveStyle(textView);
        }
        int index = item.getDebtState().getCode() % 4;
        setStateActiveStyle(debtStates[index]);
    }

    private void setStylesForDebtStateWithConfirmation(View view, Debt item) {
        TextView[] debtStates;
        debtStates = getDebtWithConfirmationViews(view);
        for (TextView textView : getDebtWithoutConfirmationViews(view)) {
            textView.setVisibility(View.GONE);
        }
        for (TextView textView : debtStates) {
            setStateInactiveStyle(textView);
        }
        int index = item.getDebtState().getCode();
        setStateActiveStyle(debtStates[index]);
    }

    private void setStateInactiveStyle(TextView textView) {
        textView.setTextAppearance(activity, R.style.statusTextStyleInactive);
        textView.setBackground(new ColorDrawable(0));
    }

    private void setStateActiveStyle(TextView debtState) {
        debtState.setTextAppearance(activity, R.style.statusTextStyleActive);
        debtState.setBackground(activity.getResources().getDrawable(R.drawable.oval));
    }

    private TextView[] getDebtWithConfirmationViews(View view) {
        return new TextView[]{
                (TextView) view.findViewById(R.id.tv_state_with_confirm_nfd_1),
                (TextView) view.findViewById(R.id.tv_state_with_confirm_cd_2),
                (TextView) view.findViewById(R.id.tv_state_with_confirm_nfdpo_3),
                (TextView) view.findViewById(R.id.tv_state_with_confirm_cdpo_4)
        };
    }

    private TextView[] getDebtWithoutConfirmationViews(View view) {
        return new TextView[]{
                (TextView) view.findViewById(R.id.tv_state_no_confirm_npod_1),
                (TextView) view.findViewById(R.id.tv_state_no_confirm_pod_2)
        };
    }

    private void initUIItems(View view) {
        mainInfoTextView = (TextView) view.findViewById(R.id.tv_main_debt_info);
        descriptionTextView = (TextView) view.findViewById(R.id.tv_debt_description);
        dateTextView = (TextView) view.findViewById(R.id.tv_debt_date);
        debtSurfaceLinearLayout = (LinearLayout) view.findViewById(R.id.context_menu);


    }

    public Debt getDebtObject(int position) {
        return getItem(position);
    }
}
