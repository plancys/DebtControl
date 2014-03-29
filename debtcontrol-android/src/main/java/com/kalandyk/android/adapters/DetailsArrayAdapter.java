package com.kalandyk.android.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kalandyk.R;
import com.kalandyk.api.model.DebtEvent;
import com.kalandyk.android.listeners.ConfirmationDecisionListener;

import java.util.List;

/**
 * Created by kamil on 11/26/13.
 */
public class DetailsArrayAdapter extends ArrayAdapter<DebtEvent> {

    private LayoutInflater layoutInflater;

    private TextView detailsMainMessageTextView;
    private TextView detailsDescTextView;

    private Activity activity;

    private ConfirmationDecisionListener confirmationDecisionListener;

    public DetailsArrayAdapter(Activity activity, List<DebtEvent> objects) {
        super(activity, R.layout.list_row_details, objects);
        layoutInflater = activity.getLayoutInflater();
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        //TODO: add action when convertView is not empty
        view = layoutInflater.inflate(R.layout.list_row_details, parent, false);

        initUIItems(view);

        generateMessagesFromDebtEvent(getItem(position));

        return view;
    }

    private void generateMessagesFromDebtEvent(DebtEvent item) {

        String message = "";

        switch (item.getEventType()){
            case DEBT_SIMPLE_ADDITION:
                message = activity.getString(R.string.details_debt_simple_addition);
                break;
            case DEBT_SIMPLE_REPAYMENT:
                message = activity.getString(R.string.details_debt_simple_repayment);
                break;
            case DEBT_ADDITION_REQUEST:
                message = activity.getString(R.string.details_debt_addition_request, item.getEventCreator().getEmail());
                break;
            case DEBT_ADDITION_APPROVING:
                message = activity.getString(R.string.details_debt_addition_approve, item.getEventCreator().getEmail());
                break;
            case DEBT_ADDITION_REJECTING:
                message = activity.getString(R.string.details_debt_addition_reject, item.getEventCreator().getEmail());
                break;
            case DEBT_REPAYMENT_REQUEST:
                message = activity.getString(R.string.details_debt_request_repaying, item.getEventCreator().getEmail());
                break;
            case DEBT_APPROVING_REPAYMENT_REQUEST:
                message = activity.getString(R.string.details_debt_request_repaying_approve, item.getEventCreator().getEmail());
                break;
            case DEBT_REJECTING_REPAYMENT_REQUEST:
                message = activity.getString(R.string.details_debt_request_repaying_reject, item.getEventCreator().getEmail());
                break;
            case DEBT_CANCELING_REPAYMENT_REQUEST:
                message = activity.getString(R.string.details_debt_cancel_repayment_request, item.getEventCreator().getEmail());
                break;

        }

        detailsMainMessageTextView.setText(message);
    }

    private void initUIItems(View view) {
        detailsMainMessageTextView = (TextView) view.findViewById(R.id.tv_details_main_info);
        detailsDescTextView = (TextView) view.findViewById(R.id.tv_details_description);
    }

}
