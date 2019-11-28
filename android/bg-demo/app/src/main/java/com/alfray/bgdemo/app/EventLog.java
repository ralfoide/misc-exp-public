package com.alfray.bgdemo.app;

import android.util.Log;
import android.view.ViewGroup;
import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alfray.bgdemo.activities.EventLogViewHolder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class EventLog {
    private static final String TAG = EventLog.class.getSimpleName();

    /** A local cache of all events. */
    @GuardedBy("mEvents")
    private final List<Event> mEvents = new ArrayList<>();

    /**
     * Adapter for notifying updates. These must occur on the UiThread, otherwise RecyclerView
     * will throw if they happen when it is updating.
     */
    private EventLogAdapter mAdapter;

    @Inject
    public EventLog() {
        Log.d(TAG, "@@ init");
    }

    /**
     * Loads the local event table from the one saved in the database.
     */
    public void load() {
        Log.d(TAG, "@@ load");
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            Log.d(TAG, "@@ loaded");
        }
    }

    public void add(String msg) {
        add(LocalDateTime.now(), msg);
    }

    public void add(LocalDateTime dateTime, String msg) {
        Log.d(TAG, "@@ add");
        Event event = new Event(dateTime, msg);
        synchronized (mEvents) {
            mEvents.add(event);
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            Log.d(TAG, "@@ added");
        }
    }

    public void clear() {
        Log.d(TAG, "@@ clear");
        synchronized (mEvents) {
            mEvents.clear();
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            Log.d(TAG, "@@ cleared");
        }
    }

    public RecyclerView.Adapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new EventLogAdapter();
        }
        return mAdapter;
    }

    public class EventLogAdapter extends RecyclerView.Adapter<EventLogViewHolder> {
        @Override
        public int getItemCount() {
            synchronized (mEvents) {
                return mEvents.size();
            }
        }

        @NonNull
        @Override
        public EventLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return EventLogViewHolder.create(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull EventLogViewHolder holder, int position) {
            Event event;
            synchronized (mEvents) {
                event = mEvents.get(position);
            }
            holder.bind(event);
        }
    }

}
