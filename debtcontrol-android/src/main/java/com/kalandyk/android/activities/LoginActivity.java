package com.kalandyk.android.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kalandyk.R;
import com.kalandyk.android.service.DataUpdateService;
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

    private DataUpdateService service;

    private Button loginButton;
    private TextView userTextView;
    private User user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = new User();
        loginButton = (Button) findViewById(R.id.bt_login);
        userTextView = (TextView) findViewById(R.id.user);
        userTextView.setText(user.toString());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(service != null){
                String userToString = service.getCurrentUser().toString();
                Log.e(AbstractActivity.TAG, userToString);
                userTextView.setText(userToString);
                }
            }
        });

//        Intent intent= new Intent(this, DataUpdateService.class);
//        bindService(intent, mConnection,
//                Context.BIND_AUTO_CREATE);
        //new DownloadUserTask().execute();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent= new Intent(this, DataUpdateService.class);
        bindService(intent, mConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder binder) {
            DataUpdateService.UpdateBinder b = (DataUpdateService.UpdateBinder) binder;
            service = b.getService();
            Toast.makeText(LoginActivity.this, "Connected", Toast.LENGTH_SHORT)
                    .show();
        }

        public void onServiceDisconnected(ComponentName className) {
            service = null;
        }
    };

   /* public void userDownloaded(User user) {
       // Log.e("", user.toString());
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
    }*/
}

