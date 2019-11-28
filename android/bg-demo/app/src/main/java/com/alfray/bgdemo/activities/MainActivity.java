package com.alfray.bgdemo.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alfray.bgdemo.R;
import com.alfray.bgdemo.app.EventLog;
import com.alfray.bgdemo.app.MainApplication;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private IMainActivityComponent mComponent;

    @Inject EventLog mEventLog;
    private RecyclerView mRecyclerView;
    private RecyclerView.AdapterDataObserver mScrollToEndObserver;

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
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "@@ onPause");
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
    }

    public void onStopBtnClick(View view) {
    }

    public void onClearBtnClick(View view) {
        mEventLog.clear();
    }
}
