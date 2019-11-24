package com.alfray.jobdemo.app;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alfray.jobdemo.activities.EventLogViewHolder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class EventLog {

    private final List<Event> mEvents = new ArrayList<>();

    private EventLogAdapter mAdapter;

    @Inject
    public EventLog() {}

    public void add(String msg) {
        mEvents.add(new Event(msg));
        if (mAdapter != null) {
            mAdapter.notifyItemInserted(mEvents.size()-1);
        }
    }

    public RecyclerView.Adapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new EventLogAdapter();
        }
        return mAdapter;
    }

    public static class Event {
        private final String mMsg;

        public Event(String msg) {
            mMsg = msg;
        }

        public String getMsg() {
            return mMsg;
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
