package com.example.headmanapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.headmanapplication.DB.DatabaseHelper;
import com.example.headmanapplication.data.WeekSchedule;

import java.time.DayOfWeek;

public class EditScheduleActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    private EditText subject1EditText;
    private EditText subject2EditText;
    private EditText subject3EditText;
    private EditText subject4EditText;
    private EditText subject5EditText;

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        // Получаем ссылки на EditText элементы из макета
        subject1EditText = findViewById(R.id.subject1_edittext);
        subject2EditText = findViewById(R.id.subject2_edittext);
        subject3EditText = findViewById(R.id.subject3_edittext);
        subject4EditText = findViewById(R.id.subject4_edittext);
        subject5EditText = findViewById(R.id.subject5_edittext);

        // Инициализируем объект DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Получаем день недели из интента
        int dayOfWeekValue = getIntent().getIntExtra("day_of_week", 0);

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
        for (DayOfWeek day : DayOfWeek.values()) {
            String subject1 = "1"; // Название первого предмета
            String subject2 = "2"; // Название второго предмета
            String subject3 = "3"; // Название третьего предмета
            String subject4 = "4"; // Название четвертого предмета
            String subject5 = "5"; // Название пятого предмета

            databaseHelper.addSchedule(day, subject1, subject2, subject3, subject4, subject5);
        }

        databaseHelper.close();
        // Устанавливаем значения EditText элементов из объекта Schedule, если объект не равен null
        if (weekSchedule != null) {
            subject1EditText.setText(weekSchedule.getSubject1());
            subject2EditText.setText(weekSchedule.getSubject2());
            subject3EditText.setText(weekSchedule.getSubject3());
            subject4EditText.setText(weekSchedule.getSubject4());
            subject5EditText.setText(weekSchedule.getSubject5());
        } else {
            Toast.makeText(this, "Расписание отсутствует в базе данных", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onSaveButtonClick(View view) {
        // Получаем день недели из интента
        int dayOfWeekValue = getIntent().getIntExtra("day_of_week", 0);

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

        // Получаем значения EditText элементов
        String subject1 = subject1EditText.getText().toString();
        String subject2 = subject2EditText.getText().toString();
        String subject3 = subject3EditText.getText().toString();
        String subject4 = subject4EditText.getText().toString();
        String subject5 = subject5EditText.getText().toString();

        // Создаем объект WeekSchedule и устанавливаем значения полей
        WeekSchedule weekSchedule = new WeekSchedule(dayOfWeek, subject1, subject2, subject3, subject4, subject5);

        // Обновляем расписание в базе данных
        boolean result = databaseHelper.updateScheduleForDay(weekSchedule);

        if (result) {
            // Если обновление прошло успешно, выводим сообщение об успехе и завершаем активность
            Toast.makeText(this, "Расписание сохранено", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Если обновление не удалось, выводим сообщение об ошибке
            Toast.makeText(this, "Ошибка сохранения расписания", Toast.LENGTH_SHORT).show();
        }
    }
}