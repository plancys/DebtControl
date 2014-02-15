package com.kalandyk.android.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.adapters.AbstractArrayAdapter;
import com.kalandyk.android.persistent.DebtDataContainer;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 1/25/14.
 */
public abstract class AbstractFragment extends Fragment {

    private AbstractDebtActivity abstractDebtActivity;
    private TextView confirmationAmountTextView;
    private LinearLayout notificationLayout;
    protected DebtDataContainer cachedData;


    protected AbstractDebtActivity getAbstractDebtActivity() {
        if (abstractDebtActivity == null) {
            abstractDebtActivity = (AbstractDebtActivity) getActivity();
        }
        return abstractDebtActivity;
    }

    public abstract AbstractArrayAdapter getFragmentArrayAdapter();

    protected void initConfirmationButton(){
        getAbstractDebtActivity().setNotificationCounter();
    }

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
        });
        return view;
    }

    public abstract View initFragment(LayoutInflater inflater, ViewGroup container);


}
