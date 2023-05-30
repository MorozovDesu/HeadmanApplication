package com.example.headmanapplication.DB;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import com.example.headmanapplication.data.Attendance;
import com.example.headmanapplication.data.User;
import com.example.headmanapplication.data.WeekSchedule;
import com.example.headmanapplication.data.Week;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "userstore_new.db"; // название бд
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
    //другая таблица
    private static final String TABLE_ATTENDANCE = "attendance";
    private static final String ATTENDANCE_COLUMN_ID = "_id";
    private static final String ATTENDANCE_COLUMN_USER_ID = "user_id";
    private static final String ATTENDANCE_COLUMN_IS_PRESENT = "is_present";

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

        String CREATE_ATTENDANCE_TABLE = "CREATE TABLE " + TABLE_ATTENDANCE + "("
                + ATTENDANCE_COLUMN_ID + " INTEGER PRIMARY KEY,"
                + ATTENDANCE_COLUMN_USER_ID + " INTEGER,"
                + ATTENDANCE_COLUMN_IS_PRESENT + " INTEGER,"
                + "FOREIGN KEY(" + ATTENDANCE_COLUMN_USER_ID + ") REFERENCES " + TABLE + "(" + COLUMN_ID + ")" +
                ")";
        db.execSQL(CREATE_ATTENDANCE_TABLE);
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
    public void addAttendance(int userId, boolean isPresent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ATTENDANCE_COLUMN_USER_ID, userId);
        values.put(ATTENDANCE_COLUMN_IS_PRESENT, isPresent ? 1 : 0);
        db.insert(TABLE_ATTENDANCE, null, values);
        db.close();
    }

    public boolean updateAttendance(int attendanceId, boolean isPresent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ATTENDANCE_COLUMN_IS_PRESENT, isPresent ? 1 : 0);

        int result = db.update(TABLE_ATTENDANCE, values, ATTENDANCE_COLUMN_ID + "=?",
                new String[]{String.valueOf(attendanceId)});

        db.close();

        return result != 0;
    }

    public List<Attendance> getAttendanceForUser(int userId) {
        List<Attendance> attendanceList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ATTENDANCE +
                " WHERE " + ATTENDANCE_COLUMN_USER_ID + "=?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                Attendance attendance = new Attendance();
                attendance.setId(Integer.parseInt(cursor.getString(0)));
                attendance.setUserId(Integer.parseInt(cursor.getString(1)));
                attendance.setPresent(cursor.getInt(2) == 1);
                attendanceList.add(attendance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return attendanceList;
    }
    //////////////////////////////////////////////////////////////////////////////////
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE,
                new String[]{COLUMN_ID, COLUMN_NAME},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") User user = new User(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));

                userList.add(user);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return userList;
    }
    public boolean isAttendancePresent(int userId) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_ATTENDANCE,
                new String[]{ATTENDANCE_COLUMN_IS_PRESENT},
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        boolean isPresent = false;

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int presentInt = cursor.getInt(cursor.getColumnIndex(ATTENDANCE_COLUMN_IS_PRESENT));
            isPresent = (presentInt == 1);
        }

        if (cursor != null) {
            cursor.close();
        }

        return isPresent;
    }




}