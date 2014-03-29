package com.kalandyk.android.task;

import android.os.AsyncTask;
import android.util.Log;
import com.kalandyk.android.activities.AbstractDebtActivity;
import com.kalandyk.android.persistent.DebtDataContainer;
import com.kalandyk.android.utils.DebtUrls;
import com.kalandyk.api.model.User;
import com.kalandyk.api.model.UserCredentials;
import com.kalandyk.api.model.wrapers.Confirmations;
import com.kalandyk.api.model.wrapers.Debts;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kamil on 2/6/14.
 */
public abstract class AbstractDebtTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    protected RestTemplate restTemplate;
    protected DebtUrls urls;
    protected DebtDataContainer cachedData;

    public AbstractDebtTask(){
        urls = new DebtUrls(getDebtActivity());
        restTemplate = getDebtActivity().getRestTemplate();
        cachedData = getDebtActivity().getCachedData();
    }
    protected abstract AbstractDebtActivity getDebtActivity();

    protected UserCredentials getUserCredentials() {
        User loggedUser = cachedData.getLoggedUser();
        if(loggedUser == null){
            return null;
        }
        UserCredentials credentials = new UserCredentials();
        credentials.setEmail(loggedUser.getEmail());
        credentials.setPassword(loggedUser.getPassword());
        return credentials;
    }

    protected void loadConfirmationsToCacheTask() {
        Confirmations confirmations = restTemplate.postForObject(urls.getUserConfirmationsUrl(), getUserCredentials(), Confirmations.class);
        if(confirmations != null){
            Log.d(getDebtActivity().TAG, "CONFIRMATION LOADED");
            cachedData.setConfirmations(confirmations.getConfirmationList());
        }
    }

    protected void loadDebtsToCacheTask() {
        Debts debts = restTemplate.postForObject(urls.getUserDebtUrl(), getUserCredentials(), Debts.class);
        if(debts != null){
            Log.d(getDebtActivity().TAG, "DEBTS LOADED");
            cachedData.setOnlineDebts(debts.getDebts());
        }
    }
}
