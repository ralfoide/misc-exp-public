package com.alfray.jobdemo.app;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.base.Objects;
import com.google.common.base.Strings;

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

    public static class EventLogViewHolder extends RecyclerView.ViewHolder {
        public static EventLogViewHolder create(@NonNull ViewGroup parent) {
            return null;
        }

        public EventLogViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

    public class EventLogAdapter extends ListAdapter<Event, EventLogViewHolder> {
        public EventLogAdapter() {
            super(DIFF_CALLBACK);
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

    public static final DiffUtil.ItemCallback<Event> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Event>() {
                @Override
                public boolean areItemsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
                    //noinspection StringEquality
                    return oldItem.getMsg() == newItem.getMsg();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
                    return oldItem.getMsg().equalsIgnoreCase(newItem.getMsg());
                }
            };
    }
}
