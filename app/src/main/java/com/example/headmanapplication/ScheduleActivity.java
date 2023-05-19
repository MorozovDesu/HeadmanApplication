package com.example.headmanapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;


public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ArrayList<DayWeek> products = new ArrayList<DayWeek>();
        if(products.size()==0){
            products.add(new DayWeek("Понедельник"));
            products.add(new DayWeek("Вторник"));
            products.add(new DayWeek("Среда"));
            products.add(new DayWeek("Четверг"));
            products.add(new DayWeek("Пятница"));
            products.add(new DayWeek("Суббота"));
        }
        ListView productList = findViewById(R.id.dayweekList);
        DayAdapter adapter = new DayAdapter(this, R.layout.day_week, products);
        productList.setAdapter(adapter);
    }

}