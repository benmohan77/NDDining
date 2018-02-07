package com.nd.blg.nddining.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nd.blg.nddining.R;
import com.nd.blg.nddining.objects.Course;
import com.nd.blg.nddining.objects.FavoritesList;
import com.nd.blg.nddining.objects.Item;
import com.nd.blg.nddining.objects.Meal;

import java.util.ArrayList;

import static com.nd.blg.nddining.R.layout.item;

/**
 * Created by Ben on 7/23/2017.
 */

public class ObjectListAdapter extends BaseAdapter {
    private ArrayList<Object> objects;
    private static  final int TYPE_MEAL = 0;
    private static final int TYPE_COURSE = 1;
    private static final int TYPE_ITEM = 2;
    private LayoutInflater inflater;
    private FavoritesList favoritesList;

    public ObjectListAdapter(Context context, ArrayList<Object> objects, FavoritesList favoritesList) {
        this.objects = objects;
        this.favoritesList = favoritesList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position) instanceof Meal){
            return TYPE_MEAL;
        }
        if (getItem(position) instanceof Course){
            return TYPE_COURSE;
        }
        return TYPE_ITEM;
    }

    @Override
    public boolean isEnabled(int position) {
        return(getItemViewType(position) == TYPE_ITEM);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        int type = getItemViewType(position);
        if(convertView == null){
            switch(type){
                case TYPE_MEAL:
                    convertView = inflater.inflate(R.layout.meal, parent, false);

                    break;
                case TYPE_COURSE:
                    convertView = inflater.inflate(R.layout.course, parent, false);

                    break;
                case TYPE_ITEM:
                    convertView = inflater.inflate(item, parent, false);
                    break;
            }
        }
        switch (type){
            case TYPE_MEAL:
                TextView meal = (TextView)convertView.findViewById(R.id.meal_name);
                TextView time = (TextView)convertView.findViewById(R.id.meal_time);
                TextView dash = (TextView)convertView.findViewById(R.id.dash);
                dash.setText("-");
                Meal m = (Meal)getItem(position);
                meal.setText(m.getName());
                time.setText(m.getTime());
                break;
            case TYPE_COURSE:
                TextView name = (TextView)convertView.findViewById(R.id.course_name);
                Course course = (Course)getItem(position);
                name.setText(course.getName());
                break;
            case TYPE_ITEM:
                TextView title = (TextView)convertView.findViewById(R.id.item_name);
                ImageView img = (ImageView)convertView.findViewById(R.id.item_fav);
                Item item = (Item) getItem(position);
                if(favoritesList.isFavorite(item.getName())){
                    img.setImageResource(R.drawable.ic_favorite_yellow_24dp);
                }else{
                    img.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }
                title.setText(item.getName());
                break;
        }
        return  convertView;
    }

}
