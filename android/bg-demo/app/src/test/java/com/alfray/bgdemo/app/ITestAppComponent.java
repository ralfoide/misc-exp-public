package com.alfray.bgdemo.app;

import android.content.Context;
import dagger.BindsInstance;
import dagger.Component;

import javax.inject.Singleton;

/**
 * A test-specific version of the {@link IMainAppComponent}.
 * This is useful for a number of reasons, typically to inject directly into a test class,
 * provide getters to expose objects normally available by inject, or to override all the
 * modules using mock/test (e.g. network, etc.)
 *
 * Robolectric tests just need to specify "@Config(application = TestApplication.class)"
 * and that custom application will install the {@link ITestAppComponent} instead of the
 * default {@link IMainAppComponent}.
 *
 * For this to build with gradle, it is necessary to add dagger and the dagger-compiler
 * in the testImplementation settings.
 *
 * Two things are typically done here:
 * - If the main component has modules, list them here either as-is or using test replacements.
 * - add inject() methods for all test classes that use @Inject.
 */
@Singleton
@Component
public interface ITestAppComponent extends IMainAppComponent {

    void inject(EventLogTest eventLogTest);

    @Component.Factory
    interface Factory {
        ITestAppComponent createTestApp(@BindsInstance @AppContext Context context);
    }
}
