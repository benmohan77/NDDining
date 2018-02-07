package com.nd.blg.nddining.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;

import com.nd.blg.nddining.recievers.AlarmReciever;
import com.nd.blg.nddining.recievers.NotificationReciever;

import java.util.Calendar;

/**
 * Created by Ben on 8/16/2017.
 */

public class BootService extends IntentService{

    public BootService() {
        super("BootService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //Stuff for Daily Alarm
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 1);

        Intent alarmIntent = new Intent(this, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 2, alarmIntent, 0);

//        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,  pendingIntent );

        //Stuff for notification Alarm
        Intent notificationIntent = new Intent(this, NotificationReciever.class);
        PendingIntent notPendingIntent = PendingIntent.getBroadcast(this, 3, notificationIntent,0 );
        AlarmManager notificationAlarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        boolean toggle = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("notifications", true);

        if(toggle){
            Calendar notCalendar = Calendar.getInstance();
//            calendar.set(Calendar.HOUR_OF_DAY, 8);
            notificationAlarmManager.setRepeating(AlarmManager.RTC, notCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY ,  pendingIntent);
        } else{
            notificationAlarmManager.cancel(notPendingIntent);
        }
    }
}
