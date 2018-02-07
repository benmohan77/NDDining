package com.nd.blg.nddining.recievers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nd.blg.nddining.R;
import com.nd.blg.nddining.activites.MainActivity;
import com.nd.blg.nddining.objects.Day;
import com.nd.blg.nddining.objects.FavoritesList;
import com.nd.blg.nddining.objects.Item;
import com.nd.blg.nddining.objects.Location;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Ben on 8/10/2017.
 */

public class NotificationReciever extends BroadcastReceiver {

    private Type type = new TypeToken<List<Day>>(){}.getType();


    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        FavoritesList favoritesList = gson.fromJson(prefs.getString(context.getResources().getString(R.string.favorites_pref), ""), FavoritesList.class);
        List<Day> week = gson.fromJson(prefs.getString(context.getResources().getString(R.string.week_pref), ""), type);
        List<Item> favorites = new ArrayList<>();
        if(week != null){
            favorites = week.get(0).favoritesFromToday(favoritesList);
        }

        Intent clickIntent = new Intent(context, MainActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(context, 4, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String outputFavs = "";
        if(favorites.isEmpty()){
            outputFavs = "None Today";
        } else{
            for (Location location: week.get(0).getLocations()) {
                if(!location.favoritesFromLocation(favoritesList).isEmpty()){
                    outputFavs = outputFavs +location.favoritesAtLocation(favoritesList) + "\n\n";
                }
            }
        }


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.dining_icon3x_white)
                .setContentText(outputFavs)
                .setContentTitle("Today's Favorites")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(outputFavs))
                .setContentIntent(pIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
