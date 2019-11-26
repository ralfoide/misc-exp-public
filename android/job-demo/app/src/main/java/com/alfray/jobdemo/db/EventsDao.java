package com.alfray.jobdemo.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventsDao {
    @Query("SELECT * FROM events")
    List<Event> getAll();

    @Insert
    void insertAll(Event... events);

    @Delete
    void deleteAll(List<Event> events);
}
