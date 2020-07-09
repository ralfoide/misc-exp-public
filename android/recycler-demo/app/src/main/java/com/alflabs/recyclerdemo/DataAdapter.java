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

/**
 * Data Adapter.
 *
 * In this example, data pages's payload is a simple consecutive integer, e.g. -2, -1, 0, 1, 2, 3...
 * These numbers are called "data values".
 * The data starts with "page 0..N-1". Then we can prepend pages (with negative data values), or
 * append pages at the end (with positive data values). These data values are the "stable IDs"
 * used by the adapter, and these values are LONGs.
 *
 * In the display, we have two *virtual* "Before" and "After" pages added before and after the
 * data pages respectively. For example when starting with 3 data values, the pager will have
 * four pages: [Before] [Page -1] [Page 0] [Page 1] [After].
 *
 * Internally the data is held in an ArrayList, in the data value / page order. For the example
 * above, the list would have 3 items: 0=[Page -1], 1=[Page 0], 2=[Page 1].
 *
 * There are 3 kind of indices used, shown for the example above:
 * - Pages Visible: [Before] [Page -1] [Page 0] [Page 1] [After]
 * - Data Values:      n/a       -1        0        1       n/a
 * - List indices:     n/a        0        1        2       n/a  -- Size = 3
 * - Adapter Position:  0         1        2        3        4   -- Size = 5
 *
 * What the 3 type of indices mean:
 * - The Recycler View / Adapter / DataHolder is only concerned with "adapter positions", which
 *   are 0-based indices representing the pages in the recycler view.
 * - The backing ArrayList is only concerned with "list indices" which are 0-based indices
 *   representing the actual data pages.
 * - The data values are purely business logic, and have no direct semantic for either the
 *   display (that's what adapter positions are for) or for backing array (that's what list indices
 *   are for).
 * - There is however a simple 1-to-1 relationship between them:
 *   - List-Index = Data-Value - (value of data at list.get(0))
 *   - Adapter-Position = List-Index + (1 "before" page)
 *   - Adapter-Size = List-Size + 2 (1 "before" page, 1 "after" page)
 *
 */
public class DataAdapter extends RecyclerView.Adapter<DataHolder> {
    private static final String TAG = "@@ " + DataAdapter.class.getSimpleName();

    public static final int VIEW_TYPE_BEFORE = -1;
    public static final int VIEW_TYPE_INT = 0;
    public static final int VIEW_TYPE_AFTER = 1;

    private static final int NUM_VIRTUAL_PAGES_BEFORE_LIST = 1;

    private final List<Data> mDataList = new ArrayList<>();
    private final IntConsumer mAddCallback;

    public DataAdapter(IntConsumer addCallback) {
        mAddCallback = addCallback;
        setHasStableIds(true);

        for (long v = Data.INITIAL_VALUE; v < Data.INITIAL_VALUE + 10; v++) {
            mDataList.add(new Data(v));
        }
    }

    /** Translates the adapter position into a list index. This does not validate the index. */
    private int adapterPositionToListIndex(int adapterPosition) {
        return adapterPosition - NUM_VIRTUAL_PAGES_BEFORE_LIST;
    }

    /** Translates the list index into an adapter position. This does not validate the position. */
    private int listIndexToAdapterPosition(int listIndex) {
        return listIndex + NUM_VIRTUAL_PAGES_BEFORE_LIST;
    }

    /** Returns the data value for the adapter position or -1 if invalid. */
    public long adapterPositionToDataValue(int adapterPosition) {
        // There are two equivalent ways to do this: just access the item and get its value
        // or get the value of item at index 0 in the list + adjust for offset.
        Data data = getData(adapterPosition);
        return data == null ? Data.NO_VALUE : data.getValue();
    }

    /** Returns the adapter position for the given data value, or -1 if invalid. */
    public int dataValueToAdapterPosition(long dataValue) {
        // We know that all data values are consecutive, so all we need is the value
        // of the first item in the list.
        long firstValue = mDataList.isEmpty() ? Data.INITIAL_VALUE : mDataList.get(0).getValue();
        int listIndex = (int) (dataValue - firstValue);
        if (listIndex >= 0 && listIndex < mDataList.size()) {
            return listIndexToAdapterPosition(listIndex);
        } else {
            return RecyclerView.NO_POSITION;
        }
    }

    @Nullable
    public Data getData(int adapterPosition) {
        int listIndex = adapterPositionToListIndex(adapterPosition);
        if (listIndex >= 0 && listIndex < mDataList.size()) {
            return mDataList.get(listIndex);
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
        return new DataHolder(root, viewType, mAddCallback);
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
        return data == null ? Data.NO_VALUE : data.getValue();
    }

    @Override
    public int getItemCount() {
        // Add +2 for page before and page after. These pages are "virtual"
        // and not stored in the data list.
        return mDataList.size() + 2;
    }

    @Override
    public int getItemViewType(int adapterPosition) {
        int listIndex = adapterPositionToListIndex(adapterPosition);
        if (listIndex == -1) {
            return VIEW_TYPE_BEFORE;
        } else if (listIndex == mDataList.size()) {
            return VIEW_TYPE_AFTER;
        } else if (listIndex >= 0 && listIndex < mDataList.size()) {
            return VIEW_TYPE_INT;
        }
        Log.e(TAG, "getItemViewType : invalid adapterPosition = " + adapterPosition);
        return RecyclerView.NO_POSITION; // this is an error
    }

    public void prependItems(int n) {
        long initialValue = mDataList.isEmpty() ? Data.INITIAL_VALUE : mDataList.get(0).getValue();
        for (int i = 0; i < n; i++) {
            mDataList.add(0, new Data(--initialValue));
        }
        // Adapter update: we do not call notifyItemRangeChanged(0, n) as not only items [0..n-1]
        // have changed, but also all the ones at [n...+] have changed too. In fact the whole list
        // has essentially changed now.
        notifyDataSetChanged();
    }

    public void appendItems(int n) {
        // Note: this expects the list to not be empty (always the case in this
        // simplified test). Proper code should handle an empty list properly.

        final int oldSize = mDataList.size();
        long lastValue = oldSize == 0 ? Data.NO_VALUE : mDataList.get(oldSize - 1).getValue();
        for (int i = 0; i < n; i++) {
            mDataList.add(new Data(++lastValue));
        }
        notifyItemRangeChanged(oldSize, n);
    }
}
