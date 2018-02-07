package com.nd.blg.nddining.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 7/22/2017.
 */

public class Meal {
    private String name;
    private List<Course> courses;
    private String time;

    public Meal(String name, String time){
        this.name = name;
        this.time = time;
        this.courses = new ArrayList<>();
    }

    public Meal(String name, String time, List<Course> courses){
        this.name = name;
        this.time = time;
        this.courses = courses;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return  this.name;
    }

    public String getTime(){
        return this.time;
    }

    public void  setTime(String time){
        this.time = time;
    }

    public void setCourses(List<Course> courses){
        this.courses = courses;
    }

    public void addCourse(Course course){
        this.courses.add(course);
    }

    public List<Course> getCourses(){
        return  this.courses;
    }

    public void sortCoursesByArray(String[] order){
        for(int i = 0; i < order.length; i++){
            for(int j = 0; j < courses.size(); j++){
                if(courses.get(j).getName().equals(order[i])){
                    courses.add(i, courses.get(j));
                    courses.remove(j + 1);
                }
            }

        }
    }

    public Course getLastCourse(){
        return courses.get(courses.size() - 1);
    }
}
