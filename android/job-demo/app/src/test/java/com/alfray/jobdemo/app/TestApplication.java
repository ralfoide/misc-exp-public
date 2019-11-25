package com.alfray.jobdemo.app;

/**
 * Creates an application for Robolectric tests.
 *
 * This derives from {@link MainApplication} but instead instantiates {@link ITestAppComponent}.
 */
public class TestApplication extends MainApplication {
    public TestApplication() {
        setComponent(DaggerITestAppComponent.factory().createTestApp());
    }

    @Override
    protected void skipThisUnderTest() {
        // no-op
    }
}
