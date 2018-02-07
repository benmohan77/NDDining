package com.nd.blg.nddining.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nd.blg.nddining.R;

import java.util.ArrayList;

/**
 * Created by Ben on 7/24/2017.
 */

public class DrawerAdapter extends ArrayAdapter<String> {

    public DrawerAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<String> strings) {
        super(context, resource, strings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String string = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_item, parent, false);
            TextView name = (TextView) convertView.findViewById(R.id.drawer_item_text);
            name.setText(string);
            ImageView img = (ImageView) convertView.findViewById(R.id.drawer_item_image);
            switch (string){
                case "Home":
                    img.setImageResource(R.drawable.ic_home_black_24dp);
                    break;
                case "Upcoming Days":
                    img.setImageResource(R.drawable.ic_event_note_black_24dp);
                    break;
                case "Favorites":
                    img.setImageResource(R.drawable.ic_favorite_black_24dp);
                    break;
                case "All Items":
                    img.setImageResource(R.drawable.ic_subject_black_24dp);
                    break;
                case "Settings":
                    img.setImageResource(R.drawable.ic_settings_black_24dp);
                    break;

            }
        }




        return  convertView;
    }
}
