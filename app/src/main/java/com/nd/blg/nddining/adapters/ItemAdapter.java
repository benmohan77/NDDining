package com.nd.blg.nddining.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nd.blg.nddining.R;
import com.nd.blg.nddining.objects.FavoritesList;
import com.nd.blg.nddining.objects.Item;

import java.util.List;

/**
 * Created by Ben on 7/23/2017.
 */

public class ItemAdapter extends ArrayAdapter<Item> {
    FavoritesList favoritesList;
    public ItemAdapter(Context context, int resource, List<Item> items, FavoritesList favoritesList) {
        super(context, resource, items);
        this.favoritesList = favoritesList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Item item = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.item_name);
        ImageView img = (ImageView) convertView.findViewById(R.id.item_fav);
        if(favoritesList.isFavorite(item.getName())){
            img.setImageResource(R.drawable.ic_favorite_yellow_24dp);
        }else{
            img.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        name.setText(item.getName());
        return  convertView;
    }

}
