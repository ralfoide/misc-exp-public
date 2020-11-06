package com.alflabs.recyclerdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
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
    private CheckBox mToggleStatusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = this;

        mToggleStatusBar = findViewById(R.id.toggle_status_bar);

        mAdapter = new DataAdapter(this::addItems);

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

        mRecyclerView.setEdgeEffectFactory(new CustomEdgeEffectFactory());
        mRecyclerView.scrollToPosition(mAdapter.dataValueToAdapterPosition(0));

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
                // Prepending items necessarily changes the current adapter position
                // as we want to keep displaying the same card yet that card is now at
                // a different adapter position.

                long currentValue = mAdapter.adapterPositionToDataValue(mCurrentAdapterPosition);

                mAdapter.prependItems(5);

                // To keep displaying the same card, we need to change the recycler view position.
                int newPos = mAdapter.dataValueToAdapterPosition(currentValue);
                if (newPos != RecyclerView.NO_POSITION) {
                    mRecyclerView.scrollToPosition(newPos);
                }

            } else {
                // Appending items is easier as it does not have any impact on the current
                // adapter position. Even if the pager were on the last "After" page, we would
                // want that page to reflect the newly added card.

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

    public void onToggleStatusBar(View view) {
        boolean enabled = mToggleStatusBar.isChecked();

        // To enable "visible under the status bar", the XML changes are:
        // - in styles.xml, add these:
        //   <item name="android:windowTranslucentStatus">true</item>
        //   <item name="android:windowDrawsSystemBarBackgrounds">true</item>
        //   <item name="android:statusBarColor">@android:color/transparent</item>
        // - in activity_main.xml (main layout), add this to the root layout:
        //   android:fitsSystemWindows="false"

        Window window = getWindow();
        View rootLayout = findViewById(R.id.root_layout);

        if (enabled) {
            window.addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            rootLayout.setFitsSystemWindows(false);
        } else {
            window.clearFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                            | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            rootLayout.setFitsSystemWindows(true);
        }

    }
}
