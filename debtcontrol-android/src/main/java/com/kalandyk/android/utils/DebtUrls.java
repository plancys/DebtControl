package com.kalandyk.android.utils;

import android.app.Activity;
import com.kalandyk.R;

/**
 * Created by kamil on 1/19/14.
 */
public class DebtUrls {

    private Activity activity;
    private String baseUrl;
    private String userFriends;


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

    public String getUserDebt(){
        return baseUrl + activity.getString(R.string.url_get_user_debts);
    }


}
