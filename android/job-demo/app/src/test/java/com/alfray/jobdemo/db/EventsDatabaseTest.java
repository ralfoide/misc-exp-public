package com.alfray.jobdemo.db;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.alfray.jobdemo.app.ITestAppComponent;
import com.alfray.jobdemo.app.MainApplication;
import com.alfray.jobdemo.app.TestApplication;
import dagger.Lazy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import javax.inject.Inject;
import java.time.LocalDateTime;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
@Config(application = TestApplication.class)
public class EventsDatabaseTest {
    @Inject EventsDatabase mEventsDatabase;

    @Before
    public void setUp() {
        ITestAppComponent component =
                (ITestAppComponent) MainApplication.getMainAppComponent(
                        ApplicationProvider.getApplicationContext());
        component.inject(this);
        assertThat(mEventsDatabase).isNotNull();
    }

    @Test
    public void getEventsDao() {
        EventsDao dao = mEventsDatabase.getEventsDao();
        assertThat(dao).isNotNull();
        assertThat(dao.getAll()).isEmpty();
    }

    @Test
    public void insert() {
        EventsDao dao = mEventsDatabase.getEventsDao();

        dao.insertAll(
                new Event(LocalDateTime.parse("2009-08-07T06:05:01"), "Msg 1"),
                new Event(LocalDateTime.parse("2009-08-07T06:05:02"), "Msg 2"));

        assertThat(dao.getAll()).hasSize(2);
        assertThat(dao.getAll().get(0).getMsg()).isEqualTo("Msg 1");
        assertThat(dao.getAll().get(0).id).isEqualTo(1);
        assertThat(dao.getAll().get(1).getMsg()).isEqualTo("Msg 2");
        assertThat(dao.getAll().get(1).id).isEqualTo(2);
    }

    @Test
    public void delete() {
        EventsDao dao = mEventsDatabase.getEventsDao();

        dao.insertAll(
                new Event(LocalDateTime.parse("2009-08-07T06:05:01"), "Msg 1"),
                new Event(LocalDateTime.parse("2009-08-07T06:05:02"), "Msg 2"));
        assertThat(dao.getAll()).hasSize(2);

        dao.deleteAll(dao.getAll());
        assertThat(dao.getAll()).isEmpty();
    }
}
