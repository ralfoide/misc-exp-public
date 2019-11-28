package com.alfray.bgdemo.app;

import java.time.LocalDateTime;

public class Event {
    private final LocalDateTime mDateTime;
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
