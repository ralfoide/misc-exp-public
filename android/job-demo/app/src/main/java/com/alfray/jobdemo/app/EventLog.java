package com.alfray.jobdemo.app;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class EventLog {

    private final List<String> mEvents = new ArrayList<>();

    @Inject
    public EventLog() {}

    public void add(String msg) {
        mEvents.add(msg);
    }
}
