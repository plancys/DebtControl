package com.kalandyk.android.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.kalandyk.android.activities.AbstractDebtActivity;

/**
 * Created by kamil on 2/9/14.
 */
public class SchedulerEventReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(AbstractDebtActivity.TAG, "SchedulerEventReceiver.onReceive() called");
        Intent eventService = new Intent(context, SchedulerEventService.class);
    }
}
