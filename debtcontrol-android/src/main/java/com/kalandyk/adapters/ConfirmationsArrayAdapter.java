package com.kalandyk.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kalandyk.R;
import com.kalandyk.api.model.Confirmation;
import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.User;
import com.kalandyk.listeners.ConfirmationDecisionListener;

import java.util.List;

/**
 * Created by kamil on 11/26/13.
 */
public class ConfirmationsArrayAdapter extends ArrayAdapter<Confirmation> {

    private LayoutInflater layoutInflater;

    private TextView confirmationMainMessageTextView;
    private TextView confirmationDescTextView;

    private Button acceptButton;
    private Button rejectButton;

    private Activity activity;

    private ConfirmationDecisionListener confirmationDecisionListener;

    public ConfirmationsArrayAdapter(Activity activity, List<Confirmation> objects) {
        super(activity, R.layout.list_row_confirmations, objects);
        layoutInflater = activity.getLayoutInflater();
        this.activity = activity;
    }

    public ConfirmationDecisionListener getConfirmationDecisionListener() {
        return confirmationDecisionListener;
    }

    public void setConfirmationDecisionListener(ConfirmationDecisionListener confirmationDecisionListener) {
        this.confirmationDecisionListener = confirmationDecisionListener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        //TODO: add action when convertView is not empty
        view = layoutInflater.inflate(R.layout.list_row_confirmations, parent, false);

        initUIItems(view);

        generateMessagesFromConfirmation(getItem(position));

        final Confirmation confirmation = getItem(position);
        initButtonsAction(confirmation);

        return view;
    }

    private void initButtonsAction(final Confirmation confirmation) {
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(confirmationDecisionListener != null){
                    confirmationDecisionListener.onAccept(confirmation);
                }
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(confirmationDecisionListener != null){
                    confirmationDecisionListener.onReject(confirmation);
                }
            }
        });
    }

    private void generateMessagesFromConfirmation(Confirmation item) {
        String message = "";
        Debt connectedDebt = item.getConnectedDebt();
        User otherSide = connectedDebt.getCreator();

        switch (item.getConfirmationType()){
            case REQUEST_DEBT_ADDING:
                //TODO: vary when user owes and lend
                message = activity.getString(R.string.confirmation_he_owes, otherSide.getLogin(), connectedDebt.getAmount(), "PLN" );
                break;
            case REQUEST_DEBT_REPAYING:
                //TODO: vary when user owes and lend
                message = activity.getString(R.string.confirmation_he_paid, otherSide.getLogin(), connectedDebt.getDescription(), connectedDebt.getAmount(), "PLN" );

        }

        confirmationMainMessageTextView.setText(message);
        confirmationDescTextView.setText(activity.getString(R.string.debt_description, connectedDebt.getDescription()));
    }

    private void initUIItems(View view) {
        confirmationMainMessageTextView = (TextView) view.findViewById(R.id.tv_confirmation_main);
        confirmationDescTextView = (TextView) view.findViewById(R.id.tv_details_description);

        acceptButton = (Button) view.findViewById(R.id.bt_confirmation_accept);
        rejectButton = (Button) view.findViewById(R.id.bt_confirmation_reject);

    }

}
