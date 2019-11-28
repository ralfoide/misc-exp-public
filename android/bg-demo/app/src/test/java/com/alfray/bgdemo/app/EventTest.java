package com.alfray.bgdemo.app;

import com.alfray.bgdemo.app.Event;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.google.common.truth.Truth.assertThat;

public class EventTest {

    @Test
    public void event_getters() {
        Event e = new Event(
                LocalDateTime.parse("2009-08-07T06:05:04", DateTimeFormatter.ISO_DATE_TIME),
                "A Message");
        assertThat(e.getMsg()).isEqualTo("A Message");
        assertThat(e.getDateTime().toString()).isEqualTo("2009-08-07T06:05:04");
    }
}
