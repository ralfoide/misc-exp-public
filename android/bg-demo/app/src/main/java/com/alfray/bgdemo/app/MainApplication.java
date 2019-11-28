package com.alfray.bgdemo.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import dagger.Lazy;

import javax.inject.Inject;
import java.time.LocalDateTime;

public class MainApplication extends Application {
    private static final String TAG = MainApplication.class.getSimpleName();

    // Note: if any injected type depends on @AppContext, it must be declared lazy, otherwise
    // dagger will try to initialize it here before the context is properly provided. This is
    // very specific to the way this application object is initialized since app context is the
    // application itself.
    @Inject Lazy<EventLog> mEventLog;
    private IMainAppComponent mAppComponent;

    public static IMainAppComponent getMainAppComponent(Context context) {
        return ((MainApplication) (context.getApplicationContext())).mAppComponent;
    }

    public MainApplication() {
        setComponent(DaggerIMainAppComponent.factory().create(this));
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
        mEventLog.get().add(LocalDateTime.now(), "App: Created");
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "@@ onTerminate");
        super.onTerminate();
    }
}
