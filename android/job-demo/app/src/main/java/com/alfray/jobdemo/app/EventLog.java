package com.alfray.jobdemo.app;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alfray.jobdemo.activities.EventLogViewHolder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class EventLog {

    private final List<Event> mEvents = new ArrayList<>();

    private EventLogAdapter mAdapter;

    @Inject
    public EventLog() {}

    public void add(String msg) {
        add(LocalDateTime.now(), msg);
    }

    public void add(LocalDateTime dateTime, String msg) {
        mEvents.add(new Event(dateTime, msg));
        if (mAdapter != null) {
            mAdapter.notifyItemInserted(mEvents.size()-1);
        }
    }

    public void clear() {
        mEvents.clear();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public RecyclerView.Adapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new EventLogAdapter();
        }
        return mAdapter;
    }

    public static class Event {
        private final LocalDateTime mDateTime;
        private final String mMsg;

        public Event(LocalDateTime dateTime, String msg) {
            mDateTime = dateTime;
            mMsg = msg;
        }

        public String getMsg() {
            return mMsg;
        }

        public LocalDateTime getDateTime() {
            return mDateTime;
        }
    }

    public class EventLogAdapter extends RecyclerView.Adapter<EventLogViewHolder> {
        @Override
        public int getItemCount() {
            return mEvents.size();
        }

        @NonNull
        @Override
        public EventLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return EventLogViewHolder.create(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull EventLogViewHolder holder, int position) {
            holder.bind(mEvents.get(position));
        }
    }
}
