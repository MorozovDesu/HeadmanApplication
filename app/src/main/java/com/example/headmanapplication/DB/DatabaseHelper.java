package com.example.headmanapplication.DB;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

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
        String selectQuery = "SELECT * FROM " + TABLE_WEEKS;
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

    // Обновление записи в таблице "week"
    public void updateWeek(Week week) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WEEKS_COLUMN_DAY_OF_WEEK, week.getDayOfWeek());
        values.put(WEEKS_COLUMN_IS_EVEN_WEEK, week.getIsEvenWeek());
        db.update(TABLE_WEEKS, values, WEEKS_COLUMN_ID + "=?", new String[]{String.valueOf(week.getId())});
        db.close();
    }

    // Удаление записи из таблицы "week"
    public void deleteWeek(Week week) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WEEKS, WEEKS_COLUMN_ID + "=?", new String[]{String.valueOf(week.getId())});
        db.close();
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
    //////////////////////////////////////////////////////////////////////////////////
}