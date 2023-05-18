package com.example.headmanapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.headmanapplication.Schedule.FridayActivity;
import com.example.headmanapplication.Schedule.MondayActivity;
import com.example.headmanapplication.Schedule.SaturdayActivity;
import com.example.headmanapplication.Schedule.ThursdayActivity;
import com.example.headmanapplication.Schedule.TuesdayActivity;
import com.example.headmanapplication.Schedule.WednesdayActivity;

public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
    }
    public void onClickMonday(View view){
        Intent intent = new Intent(this, MondayActivity.class);
        startActivity(intent);
    }
    public void onClickTuesday(View view){
        Intent intent = new Intent(this, TuesdayActivity.class);
        startActivity(intent);
    }
    public void onClickWednesday(View view){
        Intent intent = new Intent(this, WednesdayActivity.class);
        startActivity(intent);
    }
    public void onClickThursday(View view){
        Intent intent = new Intent(this, ThursdayActivity.class);
        startActivity(intent);
    }
    public void onClickFriday(View view){
        Intent intent = new Intent(this, FridayActivity.class);
        startActivity(intent);
    }
    public void onClickSaturday(View view){
        Intent intent = new Intent(this, SaturdayActivity.class);
        startActivity(intent);
    }
}