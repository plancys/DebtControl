package com.kalandyk.android.utils;

import android.app.Activity;
import com.kalandyk.R;

import java.net.URI;

/**
 * Created by kamil on 1/19/14.
 */
public class DebtUrls {

    private Activity activity;
    private String baseUrl;


    public DebtUrls(Activity activity){
        this.activity = activity;
        baseUrl = activity.getString(R.string.url_base);
    }

    public String getUserFriendsUrl(String user){
        return baseUrl + activity.getString(R.string.url_get_user_friends, user);
    }

    public String getLoginUrl(){
        return baseUrl + activity.getString(R.string.url_login);
    }

    public String getAddDebtUrl(){
        return baseUrl + activity.getString(R.string.url_add_debt);
    }

    public String getUserDebtUrl(){
        return baseUrl + activity.getString(R.string.url_get_user_debts);
    }

    public String getUserConfirmationsUrl(){
        return baseUrl + activity.getString(R.string.url_get_confirmations);
    }

    public String getSendConfirmationDecisionUrl(){
        return baseUrl + activity.getString(R.string.url_confirmation_send_decision);
    }


    public String getRequestDebtRepayingUrl() {
        return baseUrl + activity.getString(R.string.url_debt_request_repaying);
    }

    public String getCancelRepayingRequestUrl() {
        return baseUrl + activity.getString(R.string.url_cancel_debt_request_repaying);
    }

    public String getRegisterUrl(){
        return baseUrl + activity.getString(R.string.url_register);
    }
}
