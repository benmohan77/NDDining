package com.nd.blg.nddining.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 7/22/2017.
 */

public class Course {
    private String name;
    private List<Item> items;

    public Course(String name){
        this.name = name;
        this.items = new ArrayList<>();
    }

    public Course(String name, List<Item> items){
        this.name = name;
        this.items = items;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return  this.name;
    }

    public void setItems(List<Item> items){
        this.items = items;
    }

    public void addItem(Item item){
        this.items.add(item);
    }

    public List<Item> getItems(){
        return  this.items;
    }

    public Item getLastItem(){
        return items.get(items.size() - 1);
    }


}
