package com.alfray.jobdemo.activities;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;

/**
 * Robolectric 4 test for {@link MainActivity}.
 * <p/>Reference: http://robolectric.org/androidx_test/
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private ActivityScenario<MainActivity> mScenario;

    @Before
    public void setUp() {
        mScenario = ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void onCreate() {
        mScenario.moveToState(Lifecycle.State.CREATED);
        mScenario.onActivity(activity -> {
            assertThat(activity.getWindow()).isNotNull();
            assertThat(activity.getWindow().getDecorView().getWidth()).isEqualTo(320);
            assertThat(activity.getWindow().getDecorView().getHeight()).isEqualTo(470);
        });
    }
}
