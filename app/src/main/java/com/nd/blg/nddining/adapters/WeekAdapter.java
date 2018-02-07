package com.nd.blg.nddining.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nd.blg.nddining.R;
import com.nd.blg.nddining.objects.Day;

import java.util.List;

/**
 * Created by Ben on 7/23/2017.
 */

public class WeekAdapter extends ArrayAdapter<Day> {
    public WeekAdapter(Context context, int resource, List<Day> days) {
        super(context, resource, days);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Day day = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.day, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.day_name);

        name.setText(day.getFriendlyName());
        return  convertView;
    }

}
