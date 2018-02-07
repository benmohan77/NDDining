package com.nd.blg.nddining.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 7/22/2017.
 */

public class AllList {
    private List<Item> items;

    public AllList(){
        items = new ArrayList<>();
    }





    public List<Item> getAllItems(){
        return  items;
    }

    public void addItem(Item item) {
        if (!items.contains(item)) {
            items.add(item);
        }
    }

    public boolean isNewItem(Item input){
        for(Item item : items){
            if(item.getName().equals(input.getName())){
                return false;
            }
        }
        return  true;
    }

   // public void changeCourseType(String name, String type){

   // }

    public void removeItem(String name){
        for(Item item : items){
            if(item.getName().equals(name)){
                items.remove(item);
            }
        }
    }

    public boolean isItem(String name){
        for (Item item: items) {
            if(item.getName().equals(name)){
                return  true;
            }
        }
        return false;
    }

}
