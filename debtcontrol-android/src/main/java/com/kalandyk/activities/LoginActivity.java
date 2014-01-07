package com.kalandyk.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.kalandyk.R;
import com.kalandyk.api.model.User;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 1/5/14.
 */
public class LoginActivity extends Activity {

    private Button loginButton;
    private User user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.bt_login);

        new DownloadUserTask().execute();

    }

    public void userDownloaded(User user) {
        Log.e("", user.toString());
    }

    private class DownloadUserTask extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... voids) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
                messageConverters.add(new FormHttpMessageConverter());
                messageConverters.add(new StringHttpMessageConverter());
                messageConverters.add(new MappingJacksonHttpMessageConverter());
                restTemplate.setMessageConverters(messageConverters);
                final String url = "http://192.168.0.22:8080/users/admin1";
                user = restTemplate.getForObject(url, User.class);
                userDownloaded(user);
                return user;


            } catch (Exception e) {
                Log.e(AbstractActivity.TAG, e.getMessage(), e);
            }
            return null;
        }
    }
}

