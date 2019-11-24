package com.alfray.jobdemo.app;

import com.alfray.jobdemo.activities.IMainActivityComponent;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component
public interface IMainAppComponent {

    void inject(DemoReceiver demoReceiver);
    void inject(DemoJobService demoJobService);

    IMainActivityComponent.Factory newMainActivityComponent();

    @Component.Factory
    interface Factory {
        IMainAppComponent create();
    }
}
