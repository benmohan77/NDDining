package com.nd.blg.nddining.objects;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ben on 7/22/2017.
 */

public class Item {
    private String name;
    private String foodType;
    private Map<String, String> attributes;

    public Item(String name, String foodType, Map<String, String> attributes){
        this.name = name;
        this.foodType = foodType;
        this.attributes = attributes;
    }

    public Item(String name, String foodType){
        this.name = name;
        this.foodType = foodType;
        this.attributes = new HashMap<>();
    }

    public Item(String name){
        this.name = name;
        this.attributes = new HashMap<>();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return  this.name;
    }

    public void setFoodType(String foodType){
        this.foodType = foodType;
    }

    public String getFoodType(){
        return this.foodType;
    }

    public void setAttributes(Map<String, String> attributes){
        this.attributes = attributes;
    }

    public void addAttribute(String key, String value){
        this.attributes.put(key, value);
    }

    public Map<String, String> getAttributes(){
        return this.attributes;
    }

    public String getAttribute(String key){
        return attributes.get(key);
    }


}
