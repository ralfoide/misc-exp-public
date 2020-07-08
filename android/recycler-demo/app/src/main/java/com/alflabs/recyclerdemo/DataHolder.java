package com.alflabs.recyclerdemo;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

class DataHolder extends RecyclerView.ViewHolder {
    private final int mViewType;
    private final TextView mTextContent;

    @SuppressLint("SetTextI18n")
    public DataHolder(@NonNull View itemView, int viewType) {
        super(itemView);
        mViewType = viewType;
        mTextContent = itemView.findViewById(R.id.item_content);

        TextView textType = itemView.findViewById(R.id.item_type);
        switch (viewType) {
        case DataAdapter.VIEW_TYPE_BEFORE:
            textType.setText("Before Content");
            break;
        case DataAdapter.VIEW_TYPE_AFTER:
            textType.setText("After Content");
            break;
        case DataAdapter.VIEW_TYPE_INT:
            textType.setText("Card");
            break;
        default:
            textType.setText("<<< Unknown >>>");
            break;
        }
    }

    public void present(@Nullable Data data) {
        mTextContent.setText(data == null ? "NULL" : Integer.toString(data.getValue()));
    }
}
