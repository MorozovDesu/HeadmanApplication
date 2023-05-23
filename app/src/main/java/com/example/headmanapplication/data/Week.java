package com.example.headmanapplication.data;

public class Week {
    private int id;
    private int dayOfWeek;
    private int isEvenWeek;

    public Week() {
    }

    public Week(int id, int dayOfWeek, int isEvenWeek) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.isEvenWeek = isEvenWeek;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
