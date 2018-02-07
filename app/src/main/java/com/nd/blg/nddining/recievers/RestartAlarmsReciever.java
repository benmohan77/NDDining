package com.nd.blg.nddining.recievers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nd.blg.nddining.services.BootService;

import static android.content.ContentValues.TAG;

/**
 * Created by Ben on 8/16/2017.
 */

public class RestartAlarmsReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            // It is better to reset alarms using Background IntentService
            Intent i = new Intent(context, BootService.class);
            ComponentName service = context.startService(i);

            if (null == service) {
            }
            else {
            }

        } else {
        }

    }
}
