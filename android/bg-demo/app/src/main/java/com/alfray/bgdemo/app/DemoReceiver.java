package com.alfray.bgdemo.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import javax.inject.Inject;

/**
 * For demo purposes, the boot action can be triggered from adb:
 * $ adb root
 * $ adb shell am broadcast -a android.intent.action.BOOT_COMPLETED -p com.alfray.bgdemo
 * (This only works on non-production builds & emulators _without_ Play Services.)
 */
public class DemoReceiver extends BroadcastReceiver {
    private static final String TAG = DemoReceiver.class.getSimpleName();

    @Inject EventLog mEventLog;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "@@ onReceive: " + intent);
        String action = intent.getAction();

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)
             || "android.intent.action.QUICKBOOT_POWERON".equals(action)) {
            Log.d(TAG, "@@ ACTION_BOOT_COMPLETED");
            MainApplication.getMainAppComponent(context).inject(this);
            mEventLog.add("Receiver: Boot completed");
            DemoJobService.scheduleJob(context);
        }
    }
}
