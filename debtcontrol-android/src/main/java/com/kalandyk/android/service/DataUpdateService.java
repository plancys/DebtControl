package com.kalandyk.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.kalandyk.api.model.User;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamil on 1/19/14.
 */
public class DataUpdateService extends Service {

    private final Binder mBinder = new UpdateBinder();
    private User currentUser;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new MappingJacksonHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);
        final String url = "http://192.168.0.22:8080/users/getUserByName/Johny";
        currentUser = restTemplate.getForObject(url, User.class);
        //userDownloaded(user);
        //return user;

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class UpdateBinder extends Binder {
        public DataUpdateService getService() {
            return DataUpdateService.this;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
