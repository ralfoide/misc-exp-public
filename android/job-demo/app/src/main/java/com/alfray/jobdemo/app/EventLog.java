package com.alfray.jobdemo.app;

import android.util.Log;
import android.view.ViewGroup;
import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.alfray.jobdemo.activities.EventLogViewHolder;
import com.alfray.jobdemo.db.Event;
import com.alfray.jobdemo.db.EventsDao;
import com.alfray.jobdemo.db.EventsDatabase;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class EventLog {
    private static final String TAG = EventLog.class.getSimpleName();

    /** Database access. */
    private final EventsDao mEventsDao;

    /**
     * A local cache of all events. Loaded lazily on the first load.
     * This is duplicating the db storage, and not an exactly good design.
     * The rationale for having this is a tutorial/example:
     * - For cases where persistence accross app runs is not needed, the in-memory event list
     *   is good enough and a database is not needed. The adapter can be kept very simple by
     *   just being a list adapter.
     * - For cases where persistence accross app runs is required, the database is a better
     *   approach. In that case, the in-memory cache is not useful. The adapter should be in
     *   that case a proper database adapter. If a memory cache is used, it should probably be
     *   a proper mru cache and not that "load everything in memory" simplification done here.
     */
    @GuardedBy("mEvents")
    private final List<Event> mEvents = new ArrayList<>();

    /**
     * True once {@link #loadAsync} has been called once.
     * Updates are not recorded in the local event table unless it has been loaded first.
     */
    private boolean mLocalCacheLoaded;

    /**
     * Adapter for notifying updates. These must occur on the UiThread, otherwise RecyclerView
     * will throw if they happen when it is updating.
     */
    private EventLogAdapter mAdapter;

    @Inject
    public EventLog(EventsDatabase eventsDatabase) {
        Log.d(TAG, "@@ init");
        mEventsDao = eventsDatabase.getEventsDao();
    }

    /**
     * Loads the local event table from the one saved in the database.
     *
     * @param uiThreadActionOnComplete An optional action to run on the Ui Thread after the load.
     */
    public void loadAsync(@Nullable Action uiThreadActionOnComplete) {
        Log.d(TAG, "@@ load");
        Disposable unused = Completable
                .fromAction( () -> {
                    synchronized (mEvents) {
                        mEvents.clear();
                        mEvents.addAll(mEventsDao.getAll());
                        mLocalCacheLoaded = true;
                    }
                } )
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    Log.d(TAG, "@@ loaded");
                    if (uiThreadActionOnComplete != null) {
                        uiThreadActionOnComplete.run();
                    }
                });
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
                        if (mLocalCacheLoaded) {
                            mEvents.add(event);
                        }
                    }
                } )
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
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
