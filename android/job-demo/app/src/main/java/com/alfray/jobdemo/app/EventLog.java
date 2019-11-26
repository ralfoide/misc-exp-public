package com.alfray.jobdemo.app;

import android.util.Log;
import android.view.ViewGroup;
import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alfray.jobdemo.activities.EventLogViewHolder;
import com.alfray.jobdemo.db.Event;
import com.alfray.jobdemo.db.EventsDao;
import com.alfray.jobdemo.db.EventsDatabase;
import dagger.Lazy;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class EventLog {
    private static final String TAG = EventLog.class.getSimpleName();

    @GuardedBy("mEvents")
    private final List<Event> mEvents = new ArrayList<>();
    private final EventsDao mEventsDao;
    private EventLogAdapter mAdapter;

    @Inject
    public EventLog(EventsDatabase eventsDatabase) {
        Log.d(TAG, "@@ init");
        mEventsDao = eventsDatabase.getEventsDao();

        Disposable unused = Completable
                .fromAction( () -> {
                    synchronized (mEvents) {
                        mEvents.addAll(mEventsDao.getAll());
                    }
                } )
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(TAG, "@@ init loaded"));
    }

    public void add(String msg) {
        add(LocalDateTime.now(), msg);
    }

    public void add(LocalDateTime dateTime, String msg) {
        Log.d(TAG, "@@ add");
        Event event = new Event(dateTime, msg);

        Disposable result = Completable
                .fromAction( () -> {
                    synchronized (mEvents) {
                        mEventsDao.insertAll(event);
                        mEvents.add(event);
                    }
                } )
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if (mAdapter != null) {
                        mAdapter.notifyItemInserted(mEvents.size() - 1);
                    }
                    Log.d(TAG, "@@ added");
                });
    }

    public void clear() {
        Log.d(TAG, "@@ clear");
        Disposable unused = Completable
                .fromAction( () -> {
                    synchronized (mEvents) {
                        mEventsDao.deleteAll(mEventsDao.getAll());
                        mEvents.clear();
                    }
                } )
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    Log.d(TAG, "@@ cleared");
                });

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
