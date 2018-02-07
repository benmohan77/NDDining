package com.nd.blg.nddining.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 7/22/2017.
 */

public class Location {
    private String name;
    private String message;
    private List<Meal> meals;

    public Location(String name, String message){
        this.name = name;
        this.message = message;
        this.meals = new ArrayList<>();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public List<Meal> getMeals(){
        return meals;
    }

    public void setMeals(List<Meal> meals){
        this.meals = meals;
    }

    public void addMeal(Meal meal){
        this.meals.add(meal);
    }

    public List<Item> getAllItems(){
        List<Item> items = new ArrayList<>();
        for (Meal meal: meals) {
            for (Course course: meal.getCourses()) {
                for (Item item: course.getItems()) {
                    items.add(item);
                }
            }
        }
        return items;
    }

    public String favoritesAtLocation(FavoritesList favoritesList){
        String favorites = this.name + ":";
        for (Item item: getAllItems()) {
            if (favoritesList.isFavorite(item)){
                favorites = favorites + " " + item.getName() + ",";
            }
        }
        favorites = favorites.substring(0, favorites.length() - 1);
        return  favorites;
    }

    public List<Item> favoritesFromLocation(FavoritesList favoritesList){
        List<Item> todaysFavorites = new ArrayList<>();
            for (Item item: getAllItems()) {
                if(favoritesList.isFavorite(item)){
                    todaysFavorites.add(item);
                }
            }
        return todaysFavorites;
    }

    public Meal getLastMeal(){
        return meals.get(meals.size() - 1);
    }

    public ArrayList<Object> getAllList(){
        ArrayList<Object> list = new ArrayList<>();
        for (Meal meal: meals){
            list.add(meal);
            for (Course course: meal.getCourses()) {
                list.add(course);
                for (Item item: course.getItems()) {
                    list.add(item);
                }
            }
        }
        return list;
    }

}
