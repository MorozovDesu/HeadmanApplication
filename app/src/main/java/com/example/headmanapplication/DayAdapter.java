package com.example.headmanapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DayAdapter extends ArrayAdapter<DayWeek> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<DayWeek> dayList;

    public DayAdapter(Context context, int resource, ArrayList<DayWeek> days) {
        super(context, resource, days);
        this.dayList = days;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DayWeek day = dayList.get(position);

        viewHolder.dayView.setText(day.getDay());

        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something when the button is clicked
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        final Button button;
        final TextView dayView;

        ViewHolder(View view) {
            button = view.findViewById(R.id.button);
            dayView = view.findViewById(R.id.dayView);
        }
    }
}
