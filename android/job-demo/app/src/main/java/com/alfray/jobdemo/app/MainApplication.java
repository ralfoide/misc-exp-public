package com.alfray.jobdemo.app;

import android.app.Application;
import android.content.Context;

public class MainApplication extends Application {

    private MainAppComponent mMainAppComponent = DaggerMainAppComponent.create();

    public static MainAppComponent getMainAppComponent(Context context) {
        return ((MainApplication) (context.getApplicationContext())).mMainAppComponent;
    }
}
