package com.alfray.bgdemo.app;

import android.content.Context;
import com.alfray.bgdemo.activities.IMainActivityComponent;
import dagger.BindsInstance;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component
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
