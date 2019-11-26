package com.alfray.jobdemo.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alfray.jobdemo.R;
import com.alfray.jobdemo.db.Event;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;

public class EventLogViewHolder extends RecyclerView.ViewHolder {
    private final TextView mTextMsg;
    private final TextView mTextTime;

    private final static DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral(' ')
            // DateTimeFormatter.ISO_LOCAL_TIME has fractional seconds, which we don't want here.
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .appendLiteral(' ')
            .appendText(ChronoField.AMPM_OF_DAY, TextStyle.SHORT)
            .toFormatter();

    @NonNull
    public static EventLogViewHolder create(@NonNull ViewGroup parent) {
        // Note: In androix.RecyclerView.ViewHolder, the new view must NOT be added to the parent
        // here. This is done by the recycler view when the view is bound to an adapter position.
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventLogViewHolder(view);
    }

    public EventLogViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextMsg = itemView.findViewById(R.id.msg);
        mTextTime = itemView.findViewById(R.id.timestamp);
    }

    public void bind(Event event) {
        mTextMsg.setText(event.getMsg());
        mTextTime.setText(event.getDateTime().format(FORMATTER));
    }
}
