package com.shrikant.todoapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shrikant Pandhare on 1/16/16.
 */
public class ItemsAdapter extends ArrayAdapter<Item> {
    public ItemsAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    Map<String, Integer> priorityColorLookUp = new HashMap<>();
    {
        priorityColorLookUp.put("High", 0xFFFF0000);
        priorityColorLookUp.put("Medium", 0xFF0000FF);
        priorityColorLookUp.put("Low", 0xFF888888);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvItemName);
        TextView tvDueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);

        // Populate the data into the template view using the data object
        tvName.setText(item.name);

        if (item.dueDate != null) {
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String strDueDate = df.format(item.dueDate);
            tvDueDate.setText(strDueDate);
        } else {
            tvDueDate.setText("");
            tvDueDate.setHint("set due date");
        }

        tvPriority.setText(item.priority);
        if (priorityColorLookUp.containsKey(item.priority))
            tvPriority.setTextColor(priorityColorLookUp.get(item.priority));

        Log.i("adapter", tvDueDate.getText().toString());

        // Return the completed view to render on screen
        return convertView;
    }
}