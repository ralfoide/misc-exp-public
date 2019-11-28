package com.alfray.bgdemo.app;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
@Config(application = TestApplication.class)
public class DemoJobServiceTest {
    private Context mContext;
    private JobScheduler mJobScheduler;

    @Before
    public void setUp() {
        mContext = ApplicationProvider.getApplicationContext();
        mJobScheduler = (JobScheduler) mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    @Test
    public void scheduleJob() {
        assertThat(mJobScheduler.getAllPendingJobs()).isEmpty();
        PermanentService.scheduleJob(mContext);

        assertThat(mJobScheduler.getAllPendingJobs()).hasSize(1);
        JobInfo jobInfo = mJobScheduler.getAllPendingJobs().get(0);
        assertThat(jobInfo.getService().getShortClassName()).isEqualTo(".app.DemoJobService");
        assertThat(jobInfo.getMinLatencyMillis()).isEqualTo(0);
        assertThat(jobInfo.getMaxExecutionDelayMillis()).isEqualTo(1000);
    }
}
