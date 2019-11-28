package com.alfray.jobdemo.app;

import android.content.Context;
import com.alfray.jobdemo.activities.IMainActivityComponent;
import com.alfray.jobdemo.db.EventsDbModule;
import dagger.BindsInstance;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {EventsDbModule.class } )
public interface IMainAppComponent {

    void inject(MainApplication mainApplication);
    void inject(DemoReceiver demoReceiver);
    void inject(DemoJobService demoJobService);

    IMainActivityComponent.Factory newMainActivityComponent();

    @Component.Factory
    interface Factory {
        IMainAppComponent create(@BindsInstance @AppContext Context context);
    }
}
