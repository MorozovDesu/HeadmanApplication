package com.example.headmanapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClickSchedule(View view){
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
    }
    public void onClickStatistics(View view){
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }
    public void onClickGroup(View view){
        Intent intent = new Intent(this, GroupActivity.class);
        startActivity(intent);
    }

}