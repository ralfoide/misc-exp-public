package com.alfray.jobdemo.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "events")
public class Event {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "date-time")
    private final LocalDateTime mDateTime;

    @ColumnInfo(name = "msg")
    private final String mMsg;

    public Event(LocalDateTime dateTime, String msg) {
        mDateTime = dateTime;
        mMsg = msg;
    }

    public String getMsg() {
        return mMsg;
    }

    public LocalDateTime getDateTime() {
        return mDateTime;
    }
}
