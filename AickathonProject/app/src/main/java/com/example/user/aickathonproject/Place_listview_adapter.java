package com.example.user.aickathonproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Place_listview_adapter extends ArrayAdapter<Place> {

    public Place_listview_adapter(Context context, ArrayList<Place> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
       // Get the data item for this position
        Place items = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.place_listview_layout, parent, false);
        }

        TextView location_name = convertView.findViewById(R.id.location_name);
        TextView date = convertView.findViewById(R.id.date);
        TextView num_visitor = convertView.findViewById(R.id.num_visitor);

        location_name.setText(items.getLocation());

        String[] updatedTime = items.getDate().split("-");
        date.setText(updatedTime[1]);

        int total = Integer.parseInt(items.getNum_female()) + Integer.parseInt(items.getNum_male());
        num_visitor.setText(String.valueOf(total));

        return convertView;
    }
}
