package com.alfray.bgdemo.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.TaskStackBuilder;
import com.alfray.bgdemo.R;
import com.alfray.bgdemo.activities.MainActivity;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class PermanentService extends Service {
    private static final String TAG = PermanentService.class.getSimpleName();

    @Inject EventLog mEventLog;

    private volatile ServiceBinder mBinder;
    private final AtomicBoolean mThreadContinue = new AtomicBoolean();
    private Thread mThread;

    public PermanentService() {
        Log.d(TAG, "@@ New PermanentService, app: " + getApplication());
    }

    public static void _start(Context context) {
        Log.d(TAG, "@@ static _start");
        Intent intent = new Intent(context, PermanentService.class);
        context.startForegroundService(intent);
    }

    /** Binds the service. Returns true in _unbindFromActivity should be called. */
    public static boolean _bindFromActivity(Context context, ServiceConnection serviceCnx) {
        Log.d(TAG, "@@ static _bindFromActivity");
        Intent intent = new Intent(context, PermanentService.class);
        return context.bindService(intent, serviceCnx, Context.BIND_AUTO_CREATE | Context.BIND_ALLOW_OOM_MANAGEMENT);
    }

    /** Counterpart to _bindFromActivity. Must be called if and only if _bindFromActivity returned true. */
    public static void _unbindFromActivity(Context context, ServiceConnection serviceCnx) {
        Log.d(TAG, "@@ static _unbindFromActivity");
        context.unbindService(serviceCnx);
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
        mEventLog.add("Service: Bind");
        if (mBinder == null) {
            mBinder = new ServiceBinder();
        }
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "@@ onUnbind");
        return true; // we want onRebind to be called
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "@@ onRebind");
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "@@ onDestroy");
        mEventLog.add("Service: Destroy");
        stopForeground(STOP_FOREGROUND_REMOVE);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "@@ onStartCommand");
        mEventLog.add("Service: Start Command");
        callStartForeground(getApplicationContext());
        if (mThread == null) {
            mThread = new Thread(this::threadMainLoop);
            mThreadContinue.set(true);
            mThread.start();
        }
        return START_STICKY;
    }

    private void threadMainLoop() {
        Log.d(TAG, "threadMainLoop start");
        mEventLog.add("Thread: Start counting");
        long counter = 0;
        while (mThreadContinue.get()) {
            counter++;
            ServiceBinder binder = mBinder;
            if (binder != null) {
                binder.invokeCounterCallback(counter);
            }
            try {
                Thread.sleep(500 /*ms*/);
            } catch (InterruptedException e) {
                Log.w(TAG, "threadMainLoop exception", e);
            }
        }
        mEventLog.add("Thread: Stop counting");
        Log.d(TAG, "threadMainLoop end");
    }

    private void callStartForeground(Context context) {
        final String channelId = "channel42";
        NotificationChannel demoChannel = new NotificationChannel(
                channelId,
                "Demo Channel",
                NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(demoChannel);

        // Create "a pending intent with a back stack"
        // Cf. https://developer.android.com/training/notify-user/navigation
        Intent intent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pending = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context, channelId)
                .setColorized(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("Demo Content Text")
                .setContentTitle("Demo Content Title")
                .setContentIntent(pending)
                .setTicker("Demo Ticker Text")
                .setAutoCancel(false)
                .build();

        startForeground(42, notification);
    }

    public class ServiceBinder extends Binder {
        private Consumer<Long> mCounterCallback;

        /** Called by the activity to request the service to stop. */
        public void stopService() {
            mThreadContinue.set(false);
            Intent intent = new Intent(getApplicationContext(), PermanentService.class);
            getApplicationContext().stopService(intent);
        }

        /**
         * Called by the activity to set or unset the counter callback.
         * The callback is called from the background thread.
         */
        public void setCounterCallback(@Nullable Consumer<Long> counterCallback) {
            mCounterCallback = counterCallback;
        }

        /** Called by the background thread to invoke the counter callbacl, if defined. */
        private void invokeCounterCallback(long counter) {
            Consumer<Long> callback = mCounterCallback;
            if (callback != null) {
                callback.accept(counter);
            }
        }
    }

}
