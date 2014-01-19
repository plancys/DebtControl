package com.kalandyk.android.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by kamil on 1/19/14.
 */
public class ScheduleReceiver extends BroadcastReceiver {

    private static final long REPEAT_TIME = 1000 * 8;

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager service = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, StartServiceReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 10);
        service.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), REPEAT_TIME, pendingIntent);
    }
}
