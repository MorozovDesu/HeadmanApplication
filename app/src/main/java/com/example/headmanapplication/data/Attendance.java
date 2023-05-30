package com.example.headmanapplication.data;

public class Attendance {
    private int id;
    private int userId;
    private boolean isPresent;

    public Attendance() {}

    public Attendance(int id, int userId, boolean isPresent) {
        this.id = id;
        this.userId = userId;
        this.isPresent = isPresent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}