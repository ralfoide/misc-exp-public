package com.alfray.jobdemo.db;

import android.content.Context;
import androidx.room.Room;
import com.alfray.jobdemo.app.AppContext;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class EventsDbModule {
    @Singleton
    @Provides
    EventsDatabase providesEventsDatabase(@AppContext Context context) {
        return Room.databaseBuilder(context, EventsDatabase.class, "events").build();
    }
}
