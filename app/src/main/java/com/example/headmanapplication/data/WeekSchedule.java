package com.example.headmanapplication.data;

import android.os.Build;

import java.time.DayOfWeek;

public class WeekSchedule {
    private int weekId;
    private int dayOfWeek;
    private int isEvenWeek;
    private String subject1;
    private String subject2;
    private String subject3;
    private String subject4;
    private String subject5;

    public WeekSchedule(DayOfWeek dayOfWeek, String subject1, String subject2, String subject3, String subject4, String subject5) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.dayOfWeek = dayOfWeek.getValue();
        }
        this.subject1 = subject1;
        this.subject2 = subject2;
        this.subject3 = subject3;
        this.subject4 = subject4;
        this.subject5 = subject5;
    }
    public WeekSchedule(int weekId, int dayOfWeek, int isEvenWeek, String subject1, String subject2, String subject3, String subject4, String subject5) {
        this.weekId = weekId;
        this.dayOfWeek = dayOfWeek;
        this.isEvenWeek = isEvenWeek;
        this.subject1 = subject1;
        this.subject2 = subject2;
        this.subject3 = subject3;
        this.subject4 = subject4;
        this.subject5 = subject5;
    }
    public WeekSchedule() {

    }


    public int getWeekId() {
        return weekId;
    }

    public void setWeekId(int weekId) {
        this.weekId = weekId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getIsEvenWeek() {
        return isEvenWeek;
    }

    public void setIsEvenWeek(int isEvenWeek) {
        this.isEvenWeek = isEvenWeek;
    }

    public String getSubject1() {
        return subject1;
    }

    public void setSubject1(String subject1) {
        this.subject1 = subject1;
    }

    public String getSubject2() {
        return subject2;
    }

    public void setSubject2(String subject2) {
        this.subject2 = subject2;
    }

    public String getSubject3() {
        return subject3;
    }

    public void setSubject3(String subject3) {
        this.subject3 = subject3;
    }

    public String getSubject4() {
        return subject4;
    }

    public void setSubject4(String subject4) {
        this.subject4 = subject4;
    }

    public String getSubject5() {
        return subject5;
    }

    public void setSubject5(String subject5) {
        this.subject5 = subject5;
    }
}