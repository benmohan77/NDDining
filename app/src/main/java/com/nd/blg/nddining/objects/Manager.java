package com.nd.blg.nddining.objects;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nd.blg.nddining.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Ben on 7/22/2017.
 */

public class Manager {
    private List<Day> week;
    private AllList allList;
    private FavoritesList favorites;
    private Day selectedDay;
    //boolean favoritesDisabled, itemsDisabled, upcomingDisabled, settingsDisabled;
    private SharedPreferences prefs;
    private Gson gson;
   // private List<Day> oldDays;
    private Type type = new TypeToken<List<Day>>(){}.getType();

    public Manager(String name, Context context){



        gson = new Gson();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(prefs.contains(context.getResources().getString(R.string.favorites_pref))){
            FavoritesList favoritesList = gson.fromJson(prefs.getString("favorites", ""), FavoritesList.class);
            setFavorites(favoritesList);
        } else{
            favorites = new FavoritesList();
        }
        if(prefs.contains(context.getResources().getString(R.string.alllist_pref))){
            AllList allList = gson.fromJson(prefs.getString("alllist", ""), AllList.class);
            setAllList(allList);
        } else{
            allList = new AllList();
        }
        if(prefs.contains(context.getResources().getString(R.string.week_pref))){
            List<Day> week = gson.fromJson(prefs.getString("week", ""), type);
            this.week = week;
        }else{
            week = new ArrayList<>();
        }

//        if(prefs.contains(context.getResources().getString(R.string.old_pref))){
//            List<Day> oldDays = gson.fromJson(prefs.getString("old", ""), type);
//            this.oldDays = oldDays;
//        }else{
//            this.oldDays = new ArrayList<>();
//        }
        setToToday();
    }


    public Day getToday(){
        if(!week.isEmpty()){
            return week.get(0);
        } else {
            return null;
        }
    }

    public Day getDay(int index){
        return week.get(index);
    }

    public void buildAllList(InputStream input){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String current;
            while((current = in.readLine()) != null){
                Item item = makeItem(current);
                if(allList.isNewItem(item)){
                    allList.addItem(item);
                }
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void buildWeek(InputStream input){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String current;
            while((current = in.readLine()) != null){
                handleLine(current);
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    private void handleLine(String in){
        if(in.contains("$D")){
            String val = in.substring(2);
            week.add(new Day());
            Day target = week.get(week.size() - 1);
            target.setName(val);
        } else if(in.contains("$L")) {
            String val = in.substring(2);
            Day target = week.get(week.size() - 1);
            target.addLocation(new Location(val, ""));
        }else if(in.contains("$S")){
            String val = in.substring(2);
            Day target = week.get(week.size() -1);
            target.getLastLocation().setMessage(val);
        }else if(in.contains("$M")){
            String val = in.substring(2);
            String[] split = val.split("~");
            String name = split[0];
            Meal meal;
            try{
                String[] atr = split[1].split("=");
                meal = new Meal(name, atr[1]);

            }catch (IndexOutOfBoundsException e){
                meal = new Meal(name, null);
            }
            Day target = week.get(week.size() - 1);
            target.getLastLocation().addMeal(meal);
        }else if(in.contains("$C")){
            String val = in.substring(2);
            Course course = new Course(val);
            Day target = week.get(week.size() - 1);
            target.getLastLocation().getLastMeal().addCourse(course);
        }else if(in.contains("$I")){
            String val = in.substring(2);
            Day target = week.get(week.size() - 1);
            target.getLastLocation().getLastMeal().getLastCourse().addItem(makeItem(val));
        }
    }


    public FavoritesList getFavorites(){
        return  this.favorites;
    }

    private Item makeItem(String in){
        StringTokenizer tokenizer = new StringTokenizer(in, "~");
        Item item = new Item(tokenizer.nextToken());
        while(tokenizer.hasMoreElements()){
            String str = tokenizer.nextToken();
            String[] atr = str.split("=");
            item.addAttribute(atr[0], atr[1]);
        }
        return item;
    }

    public void setFavorites(FavoritesList favorites){
        this.favorites = favorites;
    }

    public void setAllList(AllList allList){
        this.allList = allList;
    }

    public void setWeek(List<Day> days){
        this.week = days;
    }

    public List<Day> getWeek(){
        return this.week;
    }

    public List<Day> getWeekWithoutToday(){
        if(!week.isEmpty()){
            return this.week.subList(1, week.size());
        } else {
            return null;
        }
    }

    public AllList getAllList(){
        return allList;
    }

    public void save(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getResources().getString(R.string.favorites_pref), gson.toJson(getFavorites())).commit();
        editor.putString(context.getResources().getString(R.string.alllist_pref), gson.toJson(getAllList())).commit();
        editor.putString(context.getResources().getString(R.string.week_pref), gson.toJson(week, type));
        editor.commit();

    }

    public void updateWeekData(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        week = new ArrayList<>();
        String filepath = context.getFilesDir() + "/" + "today.txt";

        try {
            buildWeek(new FileInputStream(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateAllList(Context context){
        String filepath = context.getFilesDir() + "/" + "all.txt";


        try {
            buildAllList(new FileInputStream(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setToToday(){
        Iterator<Day> iter = week.iterator();
        Date today = new Date();
        while(iter.hasNext()){
            Day day = iter.next();
            Date date = day.getDate();
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(today);
            cal2.setTime(date);
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
            if(sameDay) {
                break;
            } else if(day.getDate().before(today)){
                iter.remove();
            }
        }

    }


    private String convertStreamToString(java.io.InputStream is){
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


}
