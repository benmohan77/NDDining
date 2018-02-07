package com.nd.blg.nddining.activites;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.net.Uri;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nd.blg.nddining.BuildConfig;
import com.nd.blg.nddining.R;
import com.nd.blg.nddining.adapters.DrawerAdapter;

import com.nd.blg.nddining.objects.Item;
import com.nd.blg.nddining.objects.Manager;
import com.nd.blg.nddining.fragments.AllListFragment;
import com.nd.blg.nddining.fragments.DayFragment;
import com.nd.blg.nddining.fragments.FavoritesFragment;
import com.nd.blg.nddining.fragments.LocationListFragment;
import com.nd.blg.nddining.fragments.SettingsFragment;
import com.nd.blg.nddining.fragments.WeekFragment;
import com.nd.blg.nddining.recievers.AlarmReciever;
import com.nd.blg.nddining.recievers.NotificationReciever;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DayFragment.OnFragmentInteractionListener, WeekFragment.OnFavoritesFragmentInteractionListener, AllListFragment.OnFragmentInteractionListener, LocationListFragment.OnLocationFragmentInteractionListener, FavoritesFragment.OnFavoritesFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener{
    private List<Fragment> pages;
    private ArrayList<String> pageNames;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private FragmentManager fm = getSupportFragmentManager();
    private Manager manager;
    private SharedPreferences prefs;
    private AlarmManager notificationAlarmManager;

    private final String NOTIFICATION_PREF = "notifications";
    private final int NOTIFICATION_TIME = 8;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkFirstRun();
        pages = new ArrayList<>();
        pageNames = new ArrayList<>();
        pageNames.add("Home");
        pageNames.add("Upcoming Days");
        pageNames.add("Favorites");
        pageNames.add("All Items");
        pageNames.add("Settings");

        setContentView(R.layout.activity_main);

        //Setting up pages and names
        //Setting up the fragments
        DayFragment dayFragment = DayFragment.newInstance(manager.getToday());
        dayFragment.setFavorites(manager.getFavorites());

        FavoritesFragment favoritesFragment = FavoritesFragment.newInstance();
        favoritesFragment.setFavoritesList(manager.getFavorites());

        AllListFragment allListFragment = AllListFragment.newInstance(manager.getAllList());
        allListFragment.setFavoritesList(manager.getFavorites());

        WeekFragment weekFragment = WeekFragment.newInstance(manager.getWeekWithoutToday());
        weekFragment.setFavoritesList(manager.getFavorites());

        //Adding the pages
        pages.add(dayFragment);
        pages.add(weekFragment);
        pages.add(favoritesFragment);
        pages.add(allListFragment);
        pages.add(SettingsFragment.newInstance());




        //Loading first fragment
        fm.beginTransaction().replace(R.id.fragment_container, pages.get(0)).commit();

        //Setting up toolbar
        setTitle("Bison Dining");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        //Setting up Drawer list
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new DrawerAdapter(getApplicationContext(), R.layout.drawer_item, pageNames));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());




        //Setting up toolbar drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        mDrawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        toggle.syncState();


    }
    //Adding favorite from
    @Override
    public void addFavoriteFromLocation(Item item) {
        manager.getFavorites().addFavorite(item, getApplicationContext());
    }

    @Override
    public void addFavoriteFromAllList(Item item) {
        manager.getFavorites().addFavorite(item, getApplicationContext());
    }

    @Override
    public void addFavoriteFromFavoritesList(Item item) {
        manager.getFavorites().addFavorite(item, getApplicationContext());
    }

    @Override
    public void onPause(){
        manager.save(this);
        // PreferenceManager.getDefaultSharedPreferences(this).edit().putString("manager", gson.toJson(manager)).commit();
        super.onPause();
    }

    private void checkFirstRun(){
        final String VERSION_KEY = "version_code";
        final int DOESNT_EXIST = -1;
        gson = new Gson();

        int currentVersion = BuildConfig.VERSION_CODE;
        manager = new Manager("manager", this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int savedVersionCode = prefs.getInt(VERSION_KEY, DOESNT_EXIST);
        if(currentVersion == savedVersionCode){ //Case if app has run before but hasn't updated
            return;
        }else if(savedVersionCode == DOESNT_EXIST){ //Case if app hasn't run before
            prefs.edit().putInt(VERSION_KEY, currentVersion).commit();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 1);

//            Intent intent = new Intent(this, AlarmReciever.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 2, intent, 0);
//
//            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,  pendingIntent );

            onNotificationToggle();

        } else if(currentVersion > savedVersionCode){ //Case if app is running for first time after update
            return;
        }

    }

    @Override
    public void onNotificationToggle() {
        manager.save(this);
        Intent intent = new Intent(this, NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 3, intent,0 );
        notificationAlarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        boolean toggle = prefs.getBoolean(NOTIFICATION_PREF, true);

        if(toggle){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, NOTIFICATION_TIME);
            notificationAlarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY ,  pendingIntent);
           // notificationAlarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 0, pendingIntent);

            Toast.makeText(this, "Notifications Turned On", Toast.LENGTH_SHORT).show();
        } else{
            notificationAlarmManager.cancel(pendingIntent);
            Toast.makeText(this, "Notifications Turned Off", Toast.LENGTH_SHORT).show();

        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectDrawerItem(position);

        }

        private void selectDrawerItem(int position){
            if(position == 1){
                fm.popBackStackImmediate("week", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            fm.beginTransaction().replace(R.id.fragment_container, pages.get(position)).commit();
            mDrawerLayout.closeDrawers();
        }
    }

    public static String convertStreamToString(java.io.InputStream is){
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    /**
     * Unused Settings Interaction Function
     * @param uri
     */
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Item item) {

    }


}
