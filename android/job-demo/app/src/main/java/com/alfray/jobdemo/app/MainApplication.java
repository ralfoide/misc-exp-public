package com.alfray.jobdemo.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import javax.inject.Inject;
import java.time.LocalDateTime;

public class MainApplication extends Application {
    private static final String TAG = MainApplication.class.getSimpleName();

    @Inject EventLog mEventLog;
    private IMainAppComponent mAppComponent;

    public static IMainAppComponent getMainAppComponent(Context context) {
        return ((MainApplication) (context.getApplicationContext())).mAppComponent;
    }

    public MainApplication() {
        setComponent(DaggerIMainAppComponent.factory().create());
    }

    /** Sets the IMainAppComponent, done by this constructor or the one from TestApplication. */
    protected void setComponent(IMainAppComponent appComponent) {
        mAppComponent = appComponent;
        mAppComponent.inject(this);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "@@ onCreate");
        super.onCreate();
        skipThisUnderTest();
    }

    protected void skipThisUnderTest() {
        mEventLog.add(LocalDateTime.now(), "App: Created");
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "@@ onTerminate");
        super.onTerminate();
    }
}
