package com.alfray.bgdemo.activities;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alfray.bgdemo.R;
import com.alfray.bgdemo.app.DemoJobService;
import com.alfray.bgdemo.app.EventLog;
import com.alfray.bgdemo.app.MainApplication;

import javax.inject.Inject;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private IMainActivityComponent mComponent;

    @Inject EventLog mEventLog;
    private RecyclerView mRecyclerView;
    private RecyclerView.AdapterDataObserver mScrollToEndObserver;
    private TextView mEditTimeLater;
    private TimePickerFragment mTimePickerFragment;

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

        mEditTimeLater = findViewById(R.id.editTimeLater);
        mEditTimeLater.setOnClickListener(this::onEditTimeLater);
        setTimeLater(LocalTime.now().plus(5, ChronoUnit.MINUTES));
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

    public void onGenerateNowBtnClick(View view) {
        DemoJobService.scheduleJob(this);
    }

    public void onClearBtnClick(View view) {
        mEventLog.clear();
    }

    public void onGenerateLaterBtnClick(View view) {
        Object tag = mEditTimeLater.getTag();
        if (tag instanceof LocalTime) {
            DemoJobService.scheduleJobAt(this, (LocalTime) tag);
        }
    }

    private void onEditTimeLater(View view) {
        if (mTimePickerFragment == null) {
            mTimePickerFragment = new TimePickerFragment(mEditTimeLater.getTag());
            mTimePickerFragment.show(getSupportFragmentManager(), "timePicker");
        }
    }

    private void setTimeLater(LocalTime localTime) {
        Log.d(TAG, "@@ setTimeLater " + localTime);
        mTimePickerFragment = null;
        if (localTime != null) {
            mEditTimeLater.setTag(localTime);
            mEditTimeLater.setText(localTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        }
    }

    /**
     * Time picker dialog-fragment. Invokes {@code setTimeLater} in activity on done, either with
     * a time (when set) or null (when dismissed).
     */
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        private final LocalTime mOriginalTime;

        public TimePickerFragment(Object originalTime) {
            mOriginalTime = originalTime instanceof LocalTime ? (LocalTime) originalTime : null;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LocalTime time = mOriginalTime != null ? mOriginalTime : LocalTime.now();
            int hour = time.getHour();
            int minute = time.getMinute();

            Log.d(TAG, "@@ TimePickerFragment for " + hour + ":" + minute);
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onDismiss(@NonNull DialogInterface dialog) {
            Log.d(TAG, "@@ TimePickerFragment.onDismiss");
            super.onDismiss(dialog);
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).setTimeLater(null);
            }
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            LocalTime time = LocalTime.of(hourOfDay, minute);
            Log.d(TAG, "@@ TimePickerFragment.onTimeSet for " + hourOfDay + ":" + minute);
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).setTimeLater(time);
            }
        }
    }

}
