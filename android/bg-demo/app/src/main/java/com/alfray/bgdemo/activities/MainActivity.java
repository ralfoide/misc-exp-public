package com.alfray.bgdemo.activities;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alfray.bgdemo.R;
import com.alfray.bgdemo.app.EventLog;
import com.alfray.bgdemo.app.MainApplication;
import com.alfray.bgdemo.app.PermanentService;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();


    @Inject EventLog mEventLog;

    private IMainActivityComponent mComponent;
    /** Service binder, valid after a successful bind call. */
    private PermanentService.ServiceBinder mServiceBinder;
    /** Note: This is on a different lifecycle than the binder object itself. This boolean solely
     * indicates whether bindService was successful and should be matched with an unbindService
     * call. It does not matter whether the ServiceConnection callback was involved. */
    private boolean mShouldUnbind;
    private RecyclerView.AdapterDataObserver mScrollToEndObserver;
    private RecyclerView mRecyclerView;
    private View mStartBtn;
    private View mStopBtn;
    private TextView mCounterText;

    private final ServiceConnection mServiceCnx = new ServiceConnection() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "@@ onServiceConnected");
            mServiceBinder = (PermanentService.ServiceBinder) service;
            mServiceBinder.setCounterCallback(
                    counter -> runOnUiThread(() -> mCounterText.setText(Long.toString(counter))));
            updateButtons();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "@@ onServiceDisconnected");
            mServiceBinder = null;
            updateButtons();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "@@ onCreate");

        mComponent = MainApplication.getMainAppComponent(this).newMainActivityComponent().create();
        mComponent.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.eventsView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mStartBtn = findViewById(R.id.startBtn);
        mStopBtn = findViewById(R.id.stopBtn);
        mCounterText = findViewById(R.id.counter);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "@@ onResume");
        super.onResume();
        mRecyclerView.setAdapter(mEventLog.getAdapter());
        mScrollToEndObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                try {
                    mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                } catch (IllegalArgumentException e) {
                    Log.w(TAG, "smoothScrollToPosition failed", e);
                    // This happens if the size has changed between when the line above counts it
                    // and when the position change (which is async) actually happens. What is the
                    // proper way to handle this? There's got to be better than this klunky thing.
                }
            }
        };
        mRecyclerView.getAdapter().registerAdapterDataObserver(mScrollToEndObserver);
        mEventLog.load();

        startService();
        updateButtons();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "@@ onPause");

        unbindWithouStopService();

        if (mScrollToEndObserver != null) {
            mRecyclerView.getAdapter().unregisterAdapterDataObserver(mScrollToEndObserver);
            mScrollToEndObserver = null;
        }

        mRecyclerView.setAdapter(null);
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "@@ onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "@@ onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "@@ onDestroy");
        super.onDestroy();
    }

    public void onStartBtnClick(View view) {
        startService();
    }

    public void onStopBtnClick(View view) {
        unbindAndStopService();
    }

    public void onClearBtnClick(View view) {
        mEventLog.clear();
    }

    private void updateButtons() {
        mStartBtn.setEnabled(mServiceBinder == null);
        mStopBtn.setEnabled(mServiceBinder != null);
    }

    private void startService() {
        Log.d(TAG, "@@ startService, binder: " + mServiceBinder + ", cnx: " + mServiceCnx);
        if (mServiceBinder == null) {
            PermanentService._start(this);
            mShouldUnbind = PermanentService._bindFromActivity(this, mServiceCnx);
        }
    }

    private void unbindWithouStopService() {
        Log.d(TAG, "@@ unbindWithouStopService, binder: " + mServiceBinder + ", cnx: " + mServiceCnx);
        if (mServiceBinder != null) {
            mServiceBinder.setCounterCallback(null);
            mServiceBinder = null;
        }
        if (mShouldUnbind) {
            mShouldUnbind = false;
            PermanentService._unbindFromActivity(this, mServiceCnx);
        }
    }

    private void unbindAndStopService() {
        Log.d(TAG, "@@ unbindAndStopService, binder: " + mServiceBinder);
        if (mServiceBinder != null) {
            mServiceBinder.setCounterCallback(null);
            mServiceBinder.stopService();
            mServiceBinder = null;
            updateButtons();
        }
        if (mShouldUnbind) {
            mShouldUnbind = false;
            PermanentService._unbindFromActivity(this, mServiceCnx);
        }
    }
}
