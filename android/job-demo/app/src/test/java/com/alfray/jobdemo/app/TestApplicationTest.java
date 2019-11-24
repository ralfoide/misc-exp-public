package com.alfray.jobdemo.app;

import android.content.Context;
import android.os.Build;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

/**
 * Robolectric 4 test for {@link MainApplication} using {@link TestApplication}
 * and its custom {@link ITestAppComponent}.
 */
@RunWith(AndroidJUnit4.class)
@Config(application = TestApplication.class)
public class TestApplicationTest {
    private Context mAppContext;

    @Before
    public void setUp() {
        mAppContext = ApplicationProvider.getApplicationContext();
        assertThat(mAppContext).isInstanceOf(MainApplication.class);
        assertThat(mAppContext).isInstanceOf(TestApplication.class);
    }

    @Test
    public void apiIsAtLeast26() {
        assertThat(Build.VERSION.SDK_INT).isAtLeast(26);
    }

    @Test
    public void getMainAppComponent() {
        IMainAppComponent appComponent = MainApplication.getMainAppComponent(mAppContext);
        assertThat(appComponent).isNotNull();
        assertThat(appComponent).isInstanceOf(ITestAppComponent.class);
    }
}
