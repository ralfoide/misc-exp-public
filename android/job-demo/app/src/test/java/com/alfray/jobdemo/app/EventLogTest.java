package com.alfray.jobdemo.app;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import java.time.LocalDateTime;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(AndroidJUnit4.class)
@Config(application = TestApplication.class)
public class EventLogTest {
    @Inject EventLog mEventLog;

    @Before
    public void setUp() {
        ITestAppComponent component =
                (ITestAppComponent) MainApplication.getMainAppComponent(
                        ApplicationProvider.getApplicationContext());
        component.inject(this);
        assertThat(mEventLog).isNotNull();
    }

    @Test
    public void getAdapter() {
        assertThat(mEventLog.getAdapter()).isNotNull();
    }

    @Test
    public void add() {
        RecyclerView.Adapter adapter = mEventLog.getAdapter();
        assertThat(adapter.getItemCount()).isEqualTo(0);

        RecyclerView.AdapterDataObserver observer = mock(RecyclerView.AdapterDataObserver.class);
        adapter.registerAdapterDataObserver(observer);

        mEventLog.add("Msg 1");
        assertThat(adapter.getItemCount()).isEqualTo(1);
        verify(observer).onItemRangeInserted(0, 1);

        mEventLog.add("Msg 2");
        assertThat(adapter.getItemCount()).isEqualTo(2);
        verify(observer).onItemRangeInserted(1, 1);

        verifyNoMoreInteractions(observer);
    }
}
