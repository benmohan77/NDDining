package com.nd.blg.nddining.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;
import android.support.v7.preference.PreferenceManager;

import com.nd.blg.nddining.objects.Manager;
import com.nd.blg.nddining.services.DownloadService;

/**
 * Created by Ben on 8/10/2017.
 */

public class AlarmReciever extends BroadcastReceiver {

    private final String IP_URL = "http://www.tgaffaney.com/ndsudining/ip.html";
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Intent downIntent = new Intent(context, DownloadService.class);
        downIntent.putExtra("url", "http://www.tgaffaney.com/ndsudining/ip.html");
        downIntent.putExtra("reciever", new DownloadReciever(new Handler()));
        context.startService(downIntent);
    }

    private class DownloadReciever extends ResultReceiver {
        public DownloadReciever(Handler handler){
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData){
            super.onReceiveResult(resultCode, resultData);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            int daysSinceUpdate = prefs.getInt("daysSinceUpdate", 0);
            if(resultCode == DownloadService.UPDATE_COMPLETE){

                    Manager manager = new Manager("manager", context);
                    manager.updateWeekData(context);
                    manager.updateAllList(context);
                    manager.save(context);
                    daysSinceUpdate = 0;
                    editor.putInt("daysSinceUpdate", daysSinceUpdate);


            } else if(resultCode == DownloadService.UPDATE_FAIL){
                daysSinceUpdate++;
                editor.putInt("daysSinceUpdate", daysSinceUpdate);
            }
        }

    }
}
