package com.alfray.jobdemo.app;

import android.app.job.JobInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * For demo purposes, the boot action can be triggered from adb:
 * $ adb root
 * $ adb shell am broadcast -a android.intent.action.BOOT_COMPLETED -p com.alfray.jobdemo
 * (This only works on non-production builds & emulators _without_ Play Services.)
 */
public class DemoReceiver extends BroadcastReceiver {
    private static final String TAG = DemoReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + intent);
        String action = intent.getAction();

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Log.d(TAG, "ACTION_BOOT_COMPLETED");
            DemoJobService.scheduleJob(context);
        }
    }
}
