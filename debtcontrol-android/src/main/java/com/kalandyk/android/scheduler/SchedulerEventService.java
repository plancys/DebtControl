package com.kalandyk.android.scheduler;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.kalandyk.android.activities.AbstractDebtActivity;

import java.util.Date;

/**
 * Created by kamil on 2/9/14.
 */
public class SchedulerEventService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags,
                              final int startId) {
        Log.d(AbstractDebtActivity.TAG, "event received in service: " + new Date().toString());
        return Service.START_NOT_STICKY;
    }
}
