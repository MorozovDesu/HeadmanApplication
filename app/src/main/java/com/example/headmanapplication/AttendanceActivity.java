package com.example.headmanapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.headmanapplication.DB.DatabaseHelper;
import com.example.headmanapplication.data.Attendance;
import com.example.headmanapplication.data.User;

import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        databaseHelper = new DatabaseHelper(this);

       // Получение списка всех пользователей из базы данных
        userList = databaseHelper.getAllUsers();

        // Отображение списка пользователей в виде таблицы
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TableLayout tableLayout = findViewById(R.id.tableLayout);
        TableRow header = new TableRow(this);
        header.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView nameHeader = new TextView(this);
        nameHeader.setText("Имя");
        nameHeader.setGravity(Gravity.CENTER_HORIZONTAL);
        header.addView(nameHeader);

        TextView attendanceHeader = new TextView(this);
        attendanceHeader.setText("Посещаемость");
        attendanceHeader.setGravity(Gravity.CENTER_HORIZONTAL);
        header.addView(attendanceHeader);

        tableLayout.addView(header);

        for (User user : userList) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            TextView nameTextView = new TextView(this);
            nameTextView.setText(user.getName());
            nameTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            row.addView(nameTextView);

            Switch attendanceSwitch = new Switch(this);
            attendanceSwitch.setChecked(databaseHelper.isAttendancePresent(user.getId()));
            attendanceSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    databaseHelper.addAttendance(user.getId(), true);
                } else {
                    databaseHelper.updateAttendance(user.getId(), false);
                }
            });
            row.addView(attendanceSwitch);

            tableLayout.addView(row);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }
}