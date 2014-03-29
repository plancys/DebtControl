package com.kalandyk.android.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.widget.DebtActionButton;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtType;
import com.kalandyk.android.debt.action.DebtAction;
import com.kalandyk.android.debt.logic.DebtStateObject;
import com.kalandyk.android.listeners.DebtActionListener;
import com.kalandyk.api.model.DebtPosition;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by kamil on 11/26/13.
 */
public class DebtsArrayAdapter extends AbstractArrayAdapter<Debt> {

    private DebtActionListener debtActionListener;

    private LayoutInflater layoutInflater;

    private List<Debt> data;
    private AbstractDebtActivity activity;
    private TextView mainInfoTextView;

    private TextView descriptionTextView;
    private TextView dateTextView;
    private LinearLayout debtSurfaceLinearLayout;
    private LinearLayout debtActionsLayout;

    public DebtsArrayAdapter(AbstractDebtActivity context, List<Debt> objects) {
        super(context, R.layout.list_row_debts, objects);
        this.data = objects;
        this.activity = context;
        layoutInflater = context.getLayoutInflater();
    }

    public void setDebtActionListener(DebtActionListener debtActionListener) {
        this.debtActionListener = debtActionListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //TODO: add action when convertView is not empty
        View view = layoutInflater.inflate(R.layout.list_row_debts, parent, false);
        initUIItems(view);
        view.invalidate();

        final Debt currentDebt = getItem(position);

        DebtStateObject debtStateObject = new DebtStateObject(activity, currentDebt);
        setDebtMessages(currentDebt);
        setDebtState(view, currentDebt);
        setVisibilityOfDebtContextMenu(currentDebt);
        List<DebtAction> possibleDebtActions = debtStateObject.getPossibleDebtActions();

        initDebtActionButtons(currentDebt, possibleDebtActions);
        return view;
    }

    private void initDebtActionButtons(final Debt currentDebt, List<DebtAction> possibleDebtActions) {
        for (int i = 0; i < possibleDebtActions.size(); i++) {
            final DebtAction debtAction = possibleDebtActions.get(i);
            debtAction.setDebtActionListener(new DebtActionListener() {
                @Override
                public void onDetails(Debt debt) {
                    return;
                }

                @Override
                public void onChangeDebtState(Debt debt) {
                    //TODO: investigate whether other fields need to be changed
                    Log.d(AbstractDebtActivity.TAG, DebtsArrayAdapter.class.getName() + ": Debt state has changed.");
                    currentDebt.setDebtState(debt.getDebtState());
                    notifyDataSetChanged();
                }
            });
            DebtActionButton button = new DebtActionButton(getContext(), debtAction.getDebtActionButtonText());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (debtActionListener != null) {
                        debtAction.executeAction(currentDebt);
                        debtActionListener.onChangeDebtState(currentDebt);
                    }
                }
            });
            debtActionsLayout.addView(button);
        }
    }

    private void setVisibilityOfDebtContextMenu(Debt currentDebt) {
        if (currentDebt.isSelected()) {
            debtSurfaceLinearLayout.setVisibility(View.VISIBLE);
        } else {
            debtSurfaceLinearLayout.setVisibility(View.GONE);
        }
    }

    private void setDebtMessages(Debt currentDebt) {
        String message = null;
        if(currentDebt.getDebtType().equals(DebtType.DEBT_WITH_CONFIRMATION)){
            message = getDebtMessageForDebtWithConfirmation(currentDebt);
        } else {
            message = getDebtMessageForDebtWithoutConfirmation(currentDebt);
        }
        mainInfoTextView.setText(message);
        if(currentDebt.getDebtPosition().equals(DebtPosition.DEBTOR)){
            mainInfoTextView.setTextColor(Color.RED);
        }

        descriptionTextView.setText(activity.getString(R.string.debt_description, currentDebt.getDescription()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateTextView.setText(dateFormat.format(currentDebt.getCreationDate()));
    }

    private String getDebtMessageForDebtWithoutConfirmation(Debt currentDebt) {
        String message = null;
        if (currentDebt.getDebtPosition().equals(DebtPosition.DEBTOR)) {
            message = activity.getString(R.string.debt_without_confirm_owe, currentDebt.getAmount());
        } else {
            message = activity.getString(R.string.debt_without_confirm_lend, currentDebt.getAmount());
        }
        return message;
    }

    private String getDebtMessageForDebtWithConfirmation(Debt currentDebt) {
        String message = null;
        if (currentDebt.getDebtPosition().equals(DebtPosition.DEBTOR)) {
            message = activity.getString(R.string.debt_you_owe, currentDebt.getCreditor().getEmail(), currentDebt.getAmount());
        } else {
            message = activity.getString(R.string.debt_you_lend, currentDebt.getDebtor().getEmail(), currentDebt.getAmount());
        }
        return message;
    }

    protected void onClickDetailsButtonAction(Debt debt) {

    }

    protected void onClickExecuteDebtAction(Debt debt) {

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
        mainInfoTextView = (TextView) view.findViewById(R.id.tv_details_main_info);
        descriptionTextView = (TextView) view.findViewById(R.id.tv_details_description);
        dateTextView = (TextView) view.findViewById(R.id.tv_debt_action_date);
        debtSurfaceLinearLayout = (LinearLayout) view.findViewById(R.id.context_menu);
        debtActionsLayout = (LinearLayout) view.findViewById(R.id.ll_debt_actions);

    }

    public Debt getDebtObject(int position) {
        return getItem(position);
    }

    public List<Debt> getObjectList() {
        return data;
    }

    @Override
    protected DataType getAdapterDataType() {
        return DataType.DEBTS;
    }
}
