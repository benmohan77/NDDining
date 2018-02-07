package com.nd.blg.nddining.activites;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;


import com.nd.blg.nddining.objects.Manager;
import com.nd.blg.nddining.services.DownloadService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkFirstRun();

    }

    private String date;

    private void checkFirstRun(){

        final String VERSION_KEY = "version_code";

        final int DOESNT_EXIST = -1;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int savedVersionCode = prefs.getInt(VERSION_KEY, DOESNT_EXIST);
        boolean firstDownloadSuccess = prefs.getBoolean("firstDownload", false);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();
        date = dateFormat.format(cal.getTime());
        String updateDate = prefs.getString("updateDate", "");
        if(savedVersionCode == DOESNT_EXIST || !firstDownloadSuccess || !(date.equals(updateDate))){ //Case if app hasn't run before or hasn't run yet today
            Intent downIntent = new Intent(getApplicationContext(), DownloadService.class);
            downIntent.putExtra("url", "http://www.tgaffaney.com/ndsudining/ip.html");
            downIntent.putExtra("reciever", new DownloadReciever(new Handler()));
            getApplicationContext().startService(downIntent);
            return;
        } else{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private class DownloadReciever extends ResultReceiver {
        public DownloadReciever(Handler handler){
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData){
            super.onReceiveResult(resultCode, resultData);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            int daysSinceUpdate = prefs.getInt("daysSinceUpdate", 0);
            if(resultCode == DownloadService.UPDATE_COMPLETE){
                Manager manager = new Manager("manager", getApplicationContext());
                manager.updateWeekData(getApplicationContext());
                manager.updateAllList(getApplicationContext());
                manager.save(getApplicationContext());
                daysSinceUpdate = 0;
                editor.putInt("daysSinceUpdate", daysSinceUpdate).commit();
                editor.putString("updateDate", date);
                editor.putBoolean("firstDownload", true).commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else if(resultCode == DownloadService.UPDATE_FAIL){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                editor.putBoolean("firstDownload", false).commit();
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(SplashActivity.this);


                builder.setTitle("Uh Oh").setMessage("Please make sure you're connected to the internet").setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent downIntent = new Intent(getApplicationContext(), DownloadService.class);
                        downIntent.putExtra("url", "http://www.tgaffaney.com/ndsudining/ip.html");
                        downIntent.putExtra("reciever", new DownloadReciever(new Handler()));
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        getApplicationContext().startService(downIntent);
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();

            }



        }

    }
}
