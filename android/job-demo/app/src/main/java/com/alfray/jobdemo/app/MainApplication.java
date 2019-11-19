package com.alfray.jobdemo.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MainApplication extends Application {
    private static final String TAG = MainApplication.class.getSimpleName();

    private MainAppComponent mMainAppComponent = DaggerMainAppComponent.create();

    public static MainAppComponent getMainAppComponent(Context context) {
        return ((MainApplication) (context.getApplicationContext())).mMainAppComponent;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "onTerminate");
        super.onTerminate();
    }
}
