package com.kalandyk.android.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kalandyk.R;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.adapters.AbstractArrayAdapter;
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
    private ProgressDialog progressDialog;

    protected AbstractDebtActivity getAbstractDebtActivity() {
        if (abstractDebtActivity == null) {
            abstractDebtActivity = (AbstractDebtActivity) getActivity();
        }
        return abstractDebtActivity;
    }

    public abstract AbstractArrayAdapter getFragmentArrayAdapter();

//    protected ProgressDialog getProgressDialog(String message){
//        if(progressDialog == null) {
//            progressDialog = new ProgressDialog(getAbstractDebtActivity());
//            progressDialog.setTitle(getAbstractDebtActivity().getString(R.string.progress_dialog_please_wait));
//            progressDialog.setMessage(message != null ? message : getAbstractDebtActivity().getString(R.string.progress_dialog_default_message));
//        }
//        return progressDialog;
//    }

//    protected RestTemplate getRestTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//        messageConverters.add(new FormHttpMessageConverter());
//        messageConverters.add(new StringHttpMessageConverter());
//        messageConverters.add(new MappingJacksonHttpMessageConverter());
//        restTemplate.setMessageConverters(messageConverters);
//        return restTemplate;
//    }
}
