package com.nd.blg.nddining.objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 7/22/2017.
 */

public class FavoritesList {
    private ArrayList<Item> favorites;

    public FavoritesList(){
        favorites = new ArrayList<>();
    }


    public ArrayList<Item> getFavorites(){
        return  favorites;
    }

    public void addFavorite(final Item item, Context context) {
        List<Item> toRemove = new ArrayList<>();
        boolean notPresent = true;
        for (Item i: favorites) {
            if(i.getName().equals(item.getName())){
                notPresent = false;
                toRemove.add(i);
                Toast.makeText(context, "Removed " + item.getName() + " from Favorites", Toast.LENGTH_LONG);
            }
        }
        favorites.removeAll(toRemove);

        if(notPresent){
            favorites.add(item);
            Toast.makeText(context, "Added " + item.getName() + " to Favorites", Toast.LENGTH_LONG);
        }
    }

   // public void changeCourseType(String name, String type){

   // }

    public void removeFavorite(String name){
        for(Item item : favorites){
            if(item.getName().equals(name)){
                favorites.remove(item);
            }
        }
    }

    public boolean isFavorite(String name){
        for (Item item: favorites) {
            if(item.getName().equals(name)){
                return  true;
            }
        }
        return false;
    }

    public boolean isFavorite(Item item){
        return isFavorite(item.getName());
    }

    public void save(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    //    Gson gson = new Gson();
  //      prefs.edit().putString("favorites", gson.toJson(favorites));
        prefs.edit().apply();
    }
}
