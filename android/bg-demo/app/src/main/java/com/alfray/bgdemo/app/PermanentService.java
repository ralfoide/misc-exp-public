package com.alfray.bgdemo.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;

import javax.inject.Inject;

public class PermanentService extends Service {
    private static final String TAG = PermanentService.class.getSimpleName();

    @Inject EventLog mEventLog;

    public PermanentService() {
        Log.d(TAG, "@@ New PermanentService, app: " + getApplication());
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "@@ onCreate, app: " + getApplication());
        super.onCreate();
        MainApplication.getMainAppComponent(this).inject(this);
        mEventLog.add("Service: Created");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "@@ onBind");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "@@ onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "@@ onRebind");
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "@@ onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "@@ onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
}
