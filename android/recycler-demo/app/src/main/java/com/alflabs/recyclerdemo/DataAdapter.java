package com.alflabs.recyclerdemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

public class DataAdapter extends RecyclerView.Adapter<DataHolder> {
    private static final String TAG = "@@ " + DataAdapter.class.getSimpleName();

    public static final int VIEW_TYPE_BEFORE = -1;
    public static final int VIEW_TYPE_INT = 0;
    public static final int VIEW_TYPE_AFTER = 1;

    private final List<Data> mDataList = new ArrayList<>();
    private final IntConsumer mAddCallback;

    private int mAdapterPosForPageZero = 1;

    public DataAdapter(IntConsumer addCallback) {
        mAddCallback = addCallback;
        setHasStableIds(true);

        for (int i = 0; i < 10; i++) {
            mDataList.add(new Data(i));
        }
    }

    /** The adapter position for page zero. This moves are pages are prepended.
     * When starting page 0 is at index 1 because of the virtual "before" page. */
    public int getAdapterPosForPageZero() {
        return mAdapterPosForPageZero;
    }

    @Nullable
    public Data getData(int adapterPosition) {
        int index = adapterPosition - mAdapterPosForPageZero;
        if (index >= 0 && index < mDataList.size()) {
            return mDataList.get(index);
        }
        return null;
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder viewType = " + viewType);

        Context context = parent.getContext();
        View root = LayoutInflater.from(context).inflate(R.layout.recycler_view_item, parent,
                false /* attachToRoot */);
        DataHolder holder = new DataHolder(root, viewType, mAddCallback);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int adapterPosition) {
        Log.d(TAG, "onBindViewHolder adapterPosition = " + adapterPosition);
        Data data = getData(adapterPosition);
        holder.present(data);
    }

    @Override
    public long getItemId(int adapterPosition) {
        Data data = getData(adapterPosition);
        return data == null ? RecyclerView.NO_ID : data.getValue();
    }

    @Override
    public int getItemCount() {
        // Add +2 for page before and page after. These pages are "virtual"
        // and not stored in the data list.
        return mDataList.size() + 2;
    }

    @Override
    public int getItemViewType(int adapterPosition) {
        int index = adapterPosition - mAdapterPosForPageZero;
        if (index == -1) {
            return VIEW_TYPE_BEFORE;
        } else if (index == mDataList.size()) {
            return VIEW_TYPE_AFTER;
        } else if (index >= 0 && index < mDataList.size()) {
            return VIEW_TYPE_INT;
        }
        Log.e(TAG, "getItemViewType : invalid adapterPosition = " + adapterPosition);
        return RecyclerView.NO_POSITION; // this is an error
    }

    public void prependItems(int n) {
        // Note: this expects the list to not be empty (always the case in this
        // simplified test). Proper code should handle an empty list properly.

        int initialValue = mDataList.get(0).getValue();
        for (int i = 0; i < n; i++) {
            mDataList.add(0, new Data(--initialValue));
            mAdapterPosForPageZero++; // each time page 0 moves one index up in the backing data array
        }
        notifyItemRangeChanged(0, n);
    }

    public void appendItems(int n) {
        // Note: this expects the list to not be empty (always the case in this
        // simplified test). Proper code should handle an empty list properly.

        final int oldSize = mDataList.size();
        int lastValue = mDataList.get(oldSize - 1).getValue();
        for (int i = 0; i < n; i++) {
            mDataList.add(new Data(++lastValue));
        }
        notifyItemRangeChanged(oldSize, n);
    }

    /** Returns the data value for the adapter position or -1 if invalid. */
    public int adapterPositionToDataValue(int adapterPosition) {
        Data data = getData(adapterPosition);
        return data == null ? RecyclerView.NO_POSITION : data.getValue();
    }

    /** Returns the adapter position for the given data value. */
    public int dataValueToAdapterPosition(int dataValue) {
        // We know that all data values are consecutives, and "mIndexPageZero" represents


    }
}
