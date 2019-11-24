package com.alfray.jobdemo.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alfray.jobdemo.app.EventLog;

public class EventLogViewHolder extends RecyclerView.ViewHolder {
    private final TextView mTextView;

    @NonNull
    public static EventLogViewHolder create(@NonNull ViewGroup parent) {
        // Note: In androix.RecyclerView.ViewHolder, the new view must NOT be added to the parent
        // here. This is done by the recycler view when the view is bound to an adapter position.
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new EventLogViewHolder(view);
    }

    public EventLogViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextView = (TextView) itemView;
    }

    public void bind(EventLog.Event event) {
        mTextView.setText(event.getMsg());
    }
}
