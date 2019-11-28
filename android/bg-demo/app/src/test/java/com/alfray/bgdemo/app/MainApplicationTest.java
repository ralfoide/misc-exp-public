package com.alfray.bgdemo.app;

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
 * Robolectric 4 test for {@link MainApplication}.
 * <p/>
 * Reference: http://robolectric.org/androidx_test/
 * <p/>
 * For build errors indicating resources not found or config issues, consult:
 * http://robolectric.org/migrating/#migrating-to-40
 * - build.gradle: add testOptions.unitTests.includeAndroidResources = true
 * - gradle.properties: add android.enableUnitTestBinaryResources=true
 * <p/>
 * There's currently an issue when testing against API 28:
 *   NoClassDefFoundError: [Landroid/content/res/ApkAssets;
 * This happens only on _some_ machines. It's not consistent. Why? JVM issue maybe?
 *
 * To compensate, API 26 is fixed in @Config via global properties defined here:
 *   src/test/resources/robolectric.properties
 */
@RunWith(AndroidJUnit4.class)
@Config(sdk = {26 /*, 28 --> "NoClassDefFoundError: [Landroid/content/res/ApkAssets;" */})
public class MainApplicationTest {
    private Context mAppContext;

    @Before
    public void setUp() {
        mAppContext = ApplicationProvider.getApplicationContext();
        assertThat(mAppContext).isInstanceOf(MainApplication.class);
        assertThat(mAppContext).isNotInstanceOf(TestApplication.class);
    }

    @Test
    public void apiIsAtLeast26() {
        assertThat(Build.VERSION.SDK_INT).isAtLeast(26);
    }

    @Test
    public void getMainAppComponent() {
        IMainAppComponent appComponent = MainApplication.getMainAppComponent(mAppContext);
        assertThat(appComponent).isNotNull();
        assertThat(appComponent).isNotInstanceOf(ITestAppComponent.class);
    }
}
