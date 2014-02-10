package com.kalandyk.android.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.kalandyk.android.activities.AbstractDebtActivity;

import java.util.Calendar;

/**
 * Created by kamil on 2/9/14.
 */
public class SchedulerSetupReceiver extends BroadcastReceiver {

    private static final int EXEC_INTERVAL_IN_MILLISECONDS = 5 * 1000;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(AbstractDebtActivity.TAG, "-> SchedulerSetupReceiver.onReceive() called");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentToDo = new Intent(context, SchedulerEventReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentToDo, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendarNow = Calendar.getInstance();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendarNow.getTimeInMillis(), EXEC_INTERVAL_IN_MILLISECONDS, pendingIntent);
    }
}
