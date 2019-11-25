package com.alfray.jobdemo.app;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

public class DemoJobService extends JobService {
    private static final String TAG = DemoJobService.class.getSimpleName();

    @Inject EventLog mEventLog;
    private static long mNextJobId;

    public DemoJobService() {
        Log.d(TAG, "@@ New DemoJobService, app: " + getApplication());
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "@@ onCreate, app: " + getApplication());
        super.onCreate();
        MainApplication.getMainAppComponent(this).inject(this);
        mEventLog.add("Service: Created");
    }

    public static void scheduleJob(Context context) {
        Log.d(TAG, "@@ scheduleJob");
        ComponentName serviceName = new ComponentName(context, DemoJobService.class);
        mNextJobId++;
        int jobId = (int) (mNextJobId & Long.MAX_VALUE);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, serviceName);
        builder.setMinimumLatency(0 /* millis */);
        builder.setOverrideDeadline(1000 /* millis */);

        JobScheduler scheduler = context.getSystemService(JobScheduler.class);
        scheduler.schedule(builder.build());
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        // Return false to indicate the job is finished.
        // Return true if it needs to continue, which must be done in a separate thread or async
        // task, then call jobFinished(params, wantsReschedule). The system holds a wakelock.
        Log.d(TAG, "@@ onStartJob: " + params);
        mEventLog.add("Service: Start job #" + params.getJobId());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // Indicates that a pending job must be terminated, and its wakelock is being released.
        // Return true to ask for the job to be retried, false when done with it.
        Log.d(TAG, "@@ onStopJob: " + params);
        return false;
    }
}
