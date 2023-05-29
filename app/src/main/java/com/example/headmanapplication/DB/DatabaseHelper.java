package com.example.headmanapplication.DB;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import com.example.headmanapplication.data.WeekSchedule;
import com.example.headmanapplication.data.Week;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "userstore.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    public static final String TABLE = "users"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    private static final String TABLE_WEEKS = "week";
    //другая таблица
    private static final String WEEKS_COLUMN_ID = "_id";
    private static final String WEEKS_COLUMN_DAY_OF_WEEK = "day_of_week";
    private static final String WEEKS_COLUMN_IS_EVEN_WEEK = "is_even_week";
    private static final String TABLE_SCHEDULE = "schedule";
    //другая таблица
    private static final String SCHEDULE_COLUMN_ID = "_id";
    private static final String SCHEDULE_COLUMN_DAY_OF_WEEK = "day_of_week";
    private static final String SCHEDULE_COLUMN_SUBJECT_1 = "subject_1";
    private static final String SCHEDULE_COLUMN_SUBJECT_2 = "subject_2";
    private static final String SCHEDULE_COLUMN_SUBJECT_3 = "subject_3";
    private static final String SCHEDULE_COLUMN_SUBJECT_4 = "subject_4";
    private static final String SCHEDULE_COLUMN_SUBJECT_5 = "subject_5";

    private boolean isEvenChecked = false; // флаг для отображения четных/нечетных дней

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE users (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT);");
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME// добавление начальных данных
                + ") VALUES ('Том Смит');");

        //создание таблицы с днями недели
        db.execSQL("CREATE TABLE " + TABLE_WEEKS + "(" +
                WEEKS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WEEKS_COLUMN_DAY_OF_WEEK + " INTEGER," +
                WEEKS_COLUMN_IS_EVEN_WEEK + " INTEGER)");


        db.execSQL("CREATE TABLE " + TABLE_SCHEDULE + "(" +
                SCHEDULE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SCHEDULE_COLUMN_DAY_OF_WEEK + " INTEGER," +
                SCHEDULE_COLUMN_SUBJECT_1 + " TEXT," +
                SCHEDULE_COLUMN_SUBJECT_2 + " TEXT," +
                SCHEDULE_COLUMN_SUBJECT_3 + " TEXT," +
                SCHEDULE_COLUMN_SUBJECT_4 + " TEXT," +
                SCHEDULE_COLUMN_SUBJECT_5 + " TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
    //////////////////////////////////////////////////////////////////////////////////
    // Добавление записи в таблицу "week"
    public void addWeek(int dayOfWeek, int isEvenWeek) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WEEKS_COLUMN_DAY_OF_WEEK, dayOfWeek);
        values.put(WEEKS_COLUMN_IS_EVEN_WEEK, isEvenWeek);
        db.insert(TABLE_WEEKS, null, values);
        db.close();
    }

    // Получение всех записей из таблицы "week"
    public List<Week> getAllWeeks() {
        List<Week> weekList = new ArrayList<>();
        String selectQuery;
        if (isEvenChecked) {
            selectQuery = "SELECT * FROM " + TABLE_WEEKS + " WHERE " + WEEKS_COLUMN_IS_EVEN_WEEK + "=1";
        } else {
            selectQuery = "SELECT * FROM " + TABLE_WEEKS + " WHERE " + WEEKS_COLUMN_IS_EVEN_WEEK + "=0";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Week week = new Week();
                week.setId(Integer.parseInt(cursor.getString(0)));
                week.setDayOfWeek(Integer.parseInt(cursor.getString(1)));
                week.setIsEvenWeek(Integer.parseInt(cursor.getString(2)));
                weekList.add(week);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return weekList;
    }
    public void switchEven() {
        isEvenChecked = !isEvenChecked;
    }
    // Обновление записи в таблице "week"
    public void updateWeek(Week week) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WEEKS_COLUMN_DAY_OF_WEEK, week.getDayOfWeek());
        values.put(WEEKS_COLUMN_IS_EVEN_WEEK, week.getIsEvenWeek());
        db.update(TABLE_WEEKS, values, WEEKS_COLUMN_ID + "=?", new String[]{String.valueOf(week.getId())});
        db.close();
    }
    public void deleteWeek(Week week) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WEEKS, WEEKS_COLUMN_ID + "=?", new String[]{String.valueOf(week.getId())});
        db.close();
    }
    public List<WeekSchedule> getWeekSchedule() {
        List<WeekSchedule> weekScheduleList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_WEEKS +
                " LEFT JOIN " + TABLE_SCHEDULE +
                " ON " + TABLE_WEEKS + "." + WEEKS_COLUMN_DAY_OF_WEEK + " = " + TABLE_SCHEDULE + "." + SCHEDULE_COLUMN_DAY_OF_WEEK;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                WeekSchedule weekSchedule = new WeekSchedule();
                weekSchedule.setWeekId(cursor.getInt(0));
                weekSchedule.setDayOfWeek(cursor.getInt(1));
                weekSchedule.setIsEvenWeek(cursor.getInt(2));
                weekSchedule.setSubject1(cursor.getString(4));
                weekSchedule.setSubject2(cursor.getString(5));
                weekSchedule.setSubject3(cursor.getString(6));
                weekSchedule.setSubject4(cursor.getString(7));
                weekSchedule.setSubject5(cursor.getString(8));
                weekScheduleList.add(weekSchedule);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return weekScheduleList;
    }

    // Получение количества записей в таблице "week"
    public int getWeeksCount() {
        String countQuery = "SELECT * FROM " + TABLE_WEEKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    @SuppressLint("NewApi")
    public void addSchedule(DayOfWeek day, String subject1, String subject2, String subject3, String subject4, String subject5) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCHEDULE_COLUMN_DAY_OF_WEEK, day.getValue());
        values.put(SCHEDULE_COLUMN_SUBJECT_1, subject1);
        values.put(SCHEDULE_COLUMN_SUBJECT_2, subject2);
        values.put(SCHEDULE_COLUMN_SUBJECT_3, subject3);
        values.put(SCHEDULE_COLUMN_SUBJECT_4, subject4);
        values.put(SCHEDULE_COLUMN_SUBJECT_5, subject5);
        db.insert(TABLE_SCHEDULE, null, values);

        db.close();
    }
    @SuppressLint("NewApi")
    public void updateSchedule(DayOfWeek day, String subject1, String subject2, String subject3, String subject4, String subject5) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCHEDULE_COLUMN_SUBJECT_1, subject1);
        values.put(SCHEDULE_COLUMN_SUBJECT_2, subject2);
        values.put(SCHEDULE_COLUMN_SUBJECT_3, subject3);
        values.put(SCHEDULE_COLUMN_SUBJECT_4, subject4);
        values.put(SCHEDULE_COLUMN_SUBJECT_5, subject5);
        db.update(TABLE_SCHEDULE, values, SCHEDULE_COLUMN_DAY_OF_WEEK + "=?", new String[]{String.valueOf(day.getValue())});
        db.close();
    }

    @SuppressLint("NewApi")
    public void deleteSchedule(DayOfWeek day) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCHEDULE, SCHEDULE_COLUMN_DAY_OF_WEEK + "=?", new String[]{String.valueOf(day.getValue())});
        db.close();
    }
    public WeekSchedule getScheduleForDay(DayOfWeek day) {
        if (day == null) {
            return null;
        }

        WeekSchedule weekSchedule = new WeekSchedule();
        String selectQuery = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            selectQuery = "SELECT * FROM " + TABLE_SCHEDULE + " WHERE " + SCHEDULE_COLUMN_DAY_OF_WEEK + "=" + day.getValue();
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            weekSchedule.setSubject1(cursor.getString(2));
            weekSchedule.setSubject2(cursor.getString(3));
            weekSchedule.setSubject3(cursor.getString(4));
            weekSchedule.setSubject4(cursor.getString(5));
            weekSchedule.setSubject5(cursor.getString(6));
        }

        cursor.close();
        db.close();
        return weekSchedule;
    }

    public boolean updateScheduleForDay(WeekSchedule weekSchedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCHEDULE_COLUMN_SUBJECT_1, weekSchedule.getSubject1());
        values.put(SCHEDULE_COLUMN_SUBJECT_2, weekSchedule.getSubject2());
        values.put(SCHEDULE_COLUMN_SUBJECT_3, weekSchedule.getSubject3());
        values.put(SCHEDULE_COLUMN_SUBJECT_4, weekSchedule.getSubject4());
        values.put(SCHEDULE_COLUMN_SUBJECT_5, weekSchedule.getSubject5());

        int result = db.update(TABLE_SCHEDULE, values, SCHEDULE_COLUMN_DAY_OF_WEEK + "=?",
                new String[]{String.valueOf(weekSchedule.getDayOfWeek())});

        db.close();

        return result != 0;
    }


    //////////////////////////////////////////////////////////////////////////////////
}