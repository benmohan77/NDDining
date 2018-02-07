package com.nd.blg.nddining.objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Ben on 7/22/2017.
 */

public class Day {
    private  String name;
    private List<Location> locations;

    private String[] testItems = {"Burger", "Salad", "Mac & Cheese", "Pasta", "Cake", "Soup"};

    public Day(String name){
        this.name = name;
        this.locations = new ArrayList<>();
    }

    public Day(){
        this.locations = new ArrayList<>();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String getFriendlyName(){
        if(getName() != null){
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            Date date = new Date();
            try {
                date = sdf.parse(getName());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d");
            String finalDay = dateFormat.format(date);
            return finalDay;
        } else {
            return  "";
        }

    }

    public Date getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date();
        try {
            date = sdf.parse(getName());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public void setLocations(List<Location> locations){
        this.locations = locations;
    }

    public List<Location> getLocations(){
        return this.locations;
    }

    public void addLocation(Location location){
        this.locations.add(location);
    }

    public void createTestDay(){
        for(int k = 0; k < 3; k++){
            Location temp = new Location("Test Location", "Error");
            for(int j = 0; j < 3; j++){
                Meal meal = new Meal("Test Meal", "10:00a - 12:00p");
                Course course = new Course("Test Course");
                Random rand = new Random();
                for(int i = 0; i < 5; i++){
                    Item item = new Item(testItems[rand.nextInt(5)], "Entree");
                    course.addItem(item);
                }
                meal.addCourse(course);
                temp.addMeal(meal);
            }
            locations.add(temp);
        }
    }

    public void filter(String[] map){
        List<Location> filtered = new ArrayList<>();
        for(int i = 0; i < map.length; i++){
            for (int j = 0; j < locations.size(); j++){
                if(locations.get(j).getName().equals(map[i])){
                    filtered.add(locations.get(j));
                }
            }
        }
        locations = filtered;

    }

    public List<Item> favoritesFromToday(FavoritesList favoritesList){
        List<Item> todaysFavorites = new ArrayList<>();
        for (Location location: this.locations) {
            for (Item item: location.getAllItems()) {
                if(favoritesList.isFavorite(item)){
                    todaysFavorites.add(item);
                }
            }
        }
        return todaysFavorites;
    }

    public Location getLastLocation(){
        return locations.get(locations.size() - 1);
    }
}
