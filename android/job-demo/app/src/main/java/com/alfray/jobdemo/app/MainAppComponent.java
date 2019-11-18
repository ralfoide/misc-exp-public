package com.alfray.jobdemo.app;

import com.alfray.jobdemo.activities.MainActivity;
import dagger.Component;

@Component
public interface MainAppComponent {

    void inject(MainActivity mainActivity);
}
