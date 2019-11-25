package com.alfray.jobdemo.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alfray.jobdemo.R;
import com.alfray.jobdemo.app.DemoJobService;
import com.alfray.jobdemo.app.EventLog;
import com.alfray.jobdemo.app.MainApplication;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private IMainActivityComponent mComponent;

    @Inject EventLog mEventLog;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "@@ onCreate");

        mComponent = MainApplication.getMainAppComponent(this).newMainActivityComponent().create();
        mComponent.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.eventsView);
        mRecyclerView.setAdapter(mEventLog.getAdapter());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "@@ onResume");
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "@@ onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "@@ onPause");
        super.onPause();
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

    public void onGenerateBtnClick(View view) {
        DemoJobService.scheduleJob(this);
    }

    public void onClearBtnClick(View view) {
        mEventLog.clear();
    }
}
