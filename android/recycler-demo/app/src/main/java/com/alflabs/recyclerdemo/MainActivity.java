package com.alflabs.recyclerdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "@@ " + MainActivity.class.getSimpleName();

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private PagerSnapHelper mSnapHelper;
    private int mCurrentAdapterPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = this;

        mAdapter = new DataAdapter(this, mHandler, this::addItems);

        mRecyclerView = findViewById(R.id.pager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING);

        mLayoutManager = new LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false /* reverseLayout */);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.scrollToPosition(mAdapter.getAdapterPosForPageZero());

        // Keep track of the current adapter position
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    int visibleAdapterPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if (visibleAdapterPosition != RecyclerView.NO_POSITION) {
                        mCurrentAdapterPosition = visibleAdapterPosition;
                        Log.d(TAG, "Current Adapter Position = " + mCurrentAdapterPosition);
                    }
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                case RecyclerView.SCROLL_STATE_SETTLING:
                    // no-op
                    break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    private void addItems(int viewType) {
        if (viewType != DataAdapter.VIEW_TYPE_BEFORE && viewType != DataAdapter.VIEW_TYPE_AFTER) {
            Toast.makeText(
                    this,
                    "Invalid view type " + viewType,
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        final boolean before = viewType == DataAdapter.VIEW_TYPE_BEFORE;

        // For testing purposes, delay this by 5 seconds
        mHandler.postDelayed(() -> {
            if (before) {
                int currentPos = mCurrentAdapterPosition;

                mAdapter.prependItems(5);


            } else {
                mAdapter.appendItems(5);
            }

            Toast.makeText(
                    this,
                    "Added",
                    Toast.LENGTH_SHORT)
                    .show();
        }, 5*1000);

        Toast.makeText(
                this,
                "Adding data " + (before ? "before" : "after") + " in 5 seconds",
                Toast.LENGTH_LONG)
                .show();
    }
}
