package com.kalandyk.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kalandyk.android.service.DataUpdateService;

/**
 * Created by kamil on 1/19/14.
 */
public class StartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, DataUpdateService.class);
        context.startService(service);
    }
}
