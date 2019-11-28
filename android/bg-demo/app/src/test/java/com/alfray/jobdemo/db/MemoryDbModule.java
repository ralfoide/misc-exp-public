package com.alfray.jobdemo.db;

import android.content.Context;
import androidx.room.Room;
import com.alfray.jobdemo.app.AppContext;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class MemoryDbModule {
    @Singleton
    @Provides
    EventsDatabase providesEventsDatabase(@AppContext Context context) {
        return Room
                .inMemoryDatabaseBuilder(context.getApplicationContext(), EventsDatabase.class)
                .allowMainThreadQueries()
                .build();
    }
}
