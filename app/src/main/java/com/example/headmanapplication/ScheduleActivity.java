package com.example.headmanapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.headmanapplication.DB.DatabaseHelper;
import com.example.headmanapplication.data.WeekSchedule;

import java.time.DayOfWeek;

public class ScheduleActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    private TextView subject1TextView;
    private TextView subject2TextView;
    private TextView subject3TextView;
    private TextView subject4TextView;
    private TextView subject5TextView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // Получаем ссылки на TextView элементы из макета
        subject1TextView = findViewById(R.id.subject1_textview);
        subject2TextView = findViewById(R.id.subject2_textview);
        subject3TextView = findViewById(R.id.subject3_textview);
        subject4TextView = findViewById(R.id.subject4_textview);
        subject5TextView = findViewById(R.id.subject5_textview);

        // Инициализируем объект DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Получаем день недели из интента
        int dayOfWeekValue = getIntent().getIntExtra("dayOfWeek", 0);

        DayOfWeek dayOfWeek = null;
        if (dayOfWeekValue >= 1 && dayOfWeekValue <= 7) {
            // Если значение в диапазоне от 1 до 7, то создаем объект DayOfWeek
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dayOfWeek = DayOfWeek.of(dayOfWeekValue);
            }
        } else {
            // Если значение за пределами диапазона, выводим сообщение об ошибке и завершаем активность
            Toast.makeText(this, "Некорректное значение дня недели", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Получаем расписание из базы данных
        WeekSchedule weekSchedule = databaseHelper.getScheduleForDay(dayOfWeek);

        // Устанавливаем значения TextView элементов из объекта Schedule, если объект не равен null
        if (weekSchedule != null) {
            subject1TextView.setText(weekSchedule.getSubject1());
            subject2TextView.setText(weekSchedule.getSubject2());
            subject3TextView.setText(weekSchedule.getSubject3());
            subject4TextView.setText(weekSchedule.getSubject4());
            subject5TextView.setText(weekSchedule.getSubject5());
        } else {
            Toast.makeText(this, "Расписание отсутствует в базе данных", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Закрываем соединение с базой данных при уничтожении активности
        databaseHelper.close();
    }
}