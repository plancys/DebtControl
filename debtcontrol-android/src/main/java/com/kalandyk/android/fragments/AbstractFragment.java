package com.kalandyk.android.fragments;

import android.app.Fragment;
import com.kalandyk.android.activities.AbstractDebtActivity;
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

    protected AbstractDebtActivity getAbstractDebtActivity() {
        if (abstractDebtActivity == null) {
            abstractDebtActivity = (AbstractDebtActivity) getActivity();
        }
        return abstractDebtActivity;
    }

    protected RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new MappingJacksonHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }
}
