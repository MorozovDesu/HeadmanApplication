package com.example.headmanapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.headmanapplication.DB.DatabaseHelper;
import com.example.headmanapplication.data.Week;

import java.util.ArrayList;
import java.util.List;


public class DayOfWeekActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;


    private List<Week> weekList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_of_week);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch weekSwitch = findViewById(R.id.week_switch);
        TextView weekText = findViewById(R.id.week_text);
        // Установить начальное значение текста TextView на основе текущей четности недели (извлекается из Intent)
        boolean isEven = getIntent().getBooleanExtra("is_even_week", true);
        if (isEven) {
            weekText.setText("Четная неделя");
        } else {
            weekText.setText("Нечетная неделя");
        }
        weekSwitch.setChecked(isEven);
        weekSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Изменить текст TextView на основе нового состояния Switch
                if (isChecked) {
                    weekText.setText("Четная неделя");
                } else {
                    weekText.setText("Нечетная неделя");
                }

                // Обновить Intent, чтобы передать новое состояние четности недели в ScheduleActivity
                Intent intent = getIntent();
                intent.putExtra("is_even_week", isChecked);
                setIntent(intent);
            }
        });

        // Инициализирую помощник базы данных
        databaseHelper = new DatabaseHelper(this);

        // Добавить несколько тестовых данных в таблицу "week"
        if (databaseHelper.getWeeksCount() == 0) {
            for (int i = 1; i <= 6; i++) {
                databaseHelper.addWeek(i,0);
            }
            for (int i = 1; i <= 6; i++) {
                databaseHelper.addWeek(i,1);
            }
        }
        // Получаю все недели из таблицы "week"
        weekList = databaseHelper.getAllWeeks();

        // Преобразоваю список недель в список названий дней недели
        String[] daysOfWeek = new String[weekList.size()];
        for (int i = 0; i < weekList.size(); i++) {
            daysOfWeek[i] = getDayNameFromNumber(weekList.get(i).getDayOfWeek());
        }

        // Настраиваю ListView с названиями дней недели
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, daysOfWeek);
        ListView listView = findViewById(R.id.list_view_days);
        listView.setAdapter(adapter);

        // Настриваю слушатель кликов для ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Получаю выбранную неделю
                Week selectedWeek = weekList.get(position);

                // Создаю Intent для запуска ScheduleActivity
                Intent intent = new Intent(DayOfWeekActivity.this, ScheduleActivity.class);

                // Передаю значения выбранного дня недели и четности недели в ScheduleActivity
                intent.putExtra("day_of_week", selectedWeek.getDayOfWeek());
                intent.putExtra("is_even_week", selectedWeek.getIsEvenWeek());

                // Запустить ScheduleActivity
                startActivity(intent);
            }
        });

        // Установить заголовок активности на основе выбранного дня недели (если есть)
        int dayOfWeek = getIntent().getIntExtra("day_of_week", -1);
        if (dayOfWeek != -1) {
            String selectedDay = getDayNameFromNumber(dayOfWeek);
            TextView titleView = findViewById(R.id.title_view);
            titleView.setText(selectedDay);
        }
    }

    // Вспомогательный метод для преобразования номера дня недели в его имя
    private String getDayNameFromNumber(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "Понедельник";
            case 2:
                return "Вторник";
            case 3:
                return "Среда";
            case 4:
                return "Четверг";
            case 5:
                return "Пятница";
            case 6:
                return "Суббота";
            default:
                return "";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Закрыть соединение с базой данных
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}


