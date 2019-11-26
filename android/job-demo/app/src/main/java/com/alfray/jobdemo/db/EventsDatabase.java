package com.alfray.jobdemo.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Database(entities = { Event.class }, version = 1)
@TypeConverters({ EventsDatabase.Converters.class })
public abstract class EventsDatabase extends RoomDatabase {
    public abstract EventsDao getEventsDao();

    public static class Converters {
        @TypeConverter
        public static Long toTimestamp(LocalDateTime localDateTime) {
            return localDateTime.toEpochSecond(ZoneOffset.UTC);

        }

        @TypeConverter
        public static LocalDateTime fromTimestamp(Long epoch) {
            return LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC);
        }
    }
}
