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

public class DataAdapter extends RecyclerView.Adapter<DataHolder> {
    private static final String TAG = "@@ " + DataAdapter.class.getSimpleName();

    public static final int VIEW_TYPE_BEFORE = -1;
    public static final int VIEW_TYPE_INT = 0;
    public static final int VIEW_TYPE_AFTER = 1;

    private final List<Data> mDataList = new ArrayList<>();
    private final int mBase = 1;

    public DataAdapter() {
        setHasStableIds(true);

        for (int i = 0; i < 10; i++) {
            mDataList.add(new Data(mBase + i));
        }
    }

    public int getBase() {
        return mBase;
    }

    @Nullable
    public Data getData(int adapterPosition) {
        int index = adapterPosition - mBase;
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
        DataHolder holder = new DataHolder(root, viewType);
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
        return mDataList.size() + 2;
    }

    @Override
    public int getItemViewType(int adapterPosition) {
        int index = adapterPosition - mBase;
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
}
