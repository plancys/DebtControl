package com.kalandyk.android.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.adapters.AbstractArrayAdapter;
import com.kalandyk.android.persistent.DebtDataContainer;
import com.kalandyk.android.task.AbstractDebtTask;
import com.kalandyk.api.model.User;
import com.kalandyk.api.model.UserCredentials;
import com.kalandyk.api.model.wrapers.Friends;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFragment extends Fragment {

    private AbstractDebtActivity abstractDebtActivity;
    private TextView confirmationAmountTextView;
    private LinearLayout notificationLayout;
    protected DebtDataContainer cachedData;
    private ProgressDialog progressDialog;


    protected AbstractDebtActivity getAbstractDebtActivity() {
        if (abstractDebtActivity == null) {
            abstractDebtActivity = (AbstractDebtActivity) getActivity();
        }
        return abstractDebtActivity;
    }

    public abstract AbstractArrayAdapter getFragmentArrayAdapter();

    public abstract View initFragment(LayoutInflater inflater, ViewGroup container);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cachedData = getAbstractDebtActivity().getCachedData();
        View view = initFragment(inflater, container);
        view = initConfirmationNotifier(view);
        return view;
    }

    private View initConfirmationNotifier(final View view) {
        if( cachedData == null){
            return view;
        }
        final int confirmationsAmount = cachedData.getConfirmations().size();
        getAbstractDebtActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                confirmationAmountTextView = (TextView) view.findViewById(R.id.tv_notification_number);
                notificationLayout = (LinearLayout) view.findViewById(R.id.ll_notification_layout);
                setConfirmationValue(confirmationsAmount);
            }
        });
        return view;
    }

    private void setConfirmationValue(int confirmationsAmount) {
        if (notificationLayout == null) {
            return;
        }
        if (confirmationsAmount > 0) {
            notificationLayout.setVisibility(View.VISIBLE);
            confirmationAmountTextView.setText(String.valueOf(confirmationsAmount));
        } else {
            notificationLayout.setVisibility(View.GONE);
        }
    }

    public void setConfirmationCounter(int value){
        if(notificationLayout == null || confirmationAmountTextView == null){
            return;
        }
        setConfirmationValue(value);
    }

    public void refreshData() {
        progressDialog = getAbstractDebtActivity().getProgressDialog("Refreshing data");
        progressDialog.show();
        new RefreshTask().execute();
    }

    private class RefreshTask extends AbstractDebtTask<Void, Void, Void>{

        @Override
        protected AbstractDebtActivity getDebtActivity() {
            return AbstractFragment.this.getAbstractDebtActivity();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            loadConfirmationsToCacheTask();
            loadDebtsToCacheTask();
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            progressDialog.dismiss();
            AbstractFragment.this.getFragmentArrayAdapter().refreshDataInList();
        }
    }


}
