package com.kalandyk.android.task;

import android.os.AsyncTask;
import android.util.Log;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.persistent.DebtDataContainer;
import com.kalandyk.android.utils.DebtUrls;
import com.kalandyk.api.model.User;
import com.kalandyk.api.model.UserCredentials;
import com.kalandyk.api.model.security.PasswordUtils;
import com.kalandyk.api.model.wrapers.Confirmations;
import com.kalandyk.api.model.wrapers.Debts;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Objects;

public abstract class AbstractDebtTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    protected RestTemplate restTemplate;
    protected DebtUrls urls;
    protected DebtDataContainer cachedData;

    public AbstractDebtTask() {
        urls = new DebtUrls(getDebtActivity());
        restTemplate = getDebtActivity().getRestTemplate();
        cachedData = getDebtActivity().getCachedData();
    }

    public HttpEntity<Object> getAuthHeaders() {
        UserCredentials credentials = cachedData.getCredentials();
        return getHttpHeaders(credentials.getEmail(), credentials.getPassword());
    }

    public HttpEntity<Object> getAuthHeadersWithRequestObject(Object object) {
        UserCredentials credentials = cachedData.getCredentials();
        HttpHeaders httpAuthHeaders = getHttpAuthHeaders(credentials.getEmail(), credentials.getPassword());
        return new HttpEntity(object, httpAuthHeaders);
    }

    protected abstract AbstractDebtActivity getDebtActivity();

    protected UserCredentials getUserCredentials() {
        User loggedUser = cachedData.getLoggedUser();
        if (loggedUser == null) {
            return null;
        }
        UserCredentials credentials = new UserCredentials();
        credentials.setEmail(loggedUser.getEmail());
        credentials.setPassword(loggedUser.getPassword());
        return credentials;
    }

    protected void loadConfirmationsToCacheTask() {
        Confirmations confirmations = null;
//  Confirmations confirmations = restTemplate.postForObject(urls.getUserConfirmationsUrl(), getUserCredentials(), Confirmations.class);
        ResponseEntity<Confirmations> responseConfirmations = restTemplate
                .exchange(urls.getUserConfirmationsUrl(), HttpMethod.GET, getAuthHeaders(), Confirmations.class);
        confirmations = responseConfirmations.getBody();
        if (confirmations != null) {
            Log.d(getDebtActivity().TAG, "CONFIRMATION LOADED");
            cachedData.setConfirmations(confirmations.getConfirmationList());
        }
    }

    protected void loadDebtsToCacheTask() {
        ResponseEntity<Debts> responseDebts = restTemplate
                .exchange(urls.getUserDebtUrl(), HttpMethod.GET, getAuthHeaders(), Debts.class);

        Debts debts = responseDebts.getBody();
        if (debts != null) {
            Log.d(getDebtActivity().TAG, "DEBTS LOADED");
            cachedData.setOnlineDebts(debts.getDebts());
        }
    }

    private HttpEntity<Object> getHttpHeaders(String username, String password) {
        HttpHeaders requestHeaders = getHttpAuthHeaders(username, password);
        return new HttpEntity(requestHeaders);
    }

    private HttpHeaders getHttpAuthHeaders(String username, String password) {
        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return requestHeaders;
    }
}
