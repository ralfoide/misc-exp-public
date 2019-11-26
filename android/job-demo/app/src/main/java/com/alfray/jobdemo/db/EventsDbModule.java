package com.alfray.jobdemo.db;

import android.content.Context;
import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class EventsDbModule {
    @Singleton
    @Provides
    EventsDatabase providesEventsDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), EventsDatabase.class, "events").build();
    }
}
