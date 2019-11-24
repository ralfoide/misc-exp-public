package com.alfray.jobdemo.app;

import com.alfray.jobdemo.activities.MainActivity;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component
public interface IMainAppComponent {
    void inject(MainActivity mainActivity);
    void inject(DemoReceiver demoReceiver);
    void inject(DemoJobService demoJobService);
}
