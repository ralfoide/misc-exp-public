package com.alflabs.recyclerdemo;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.function.IntConsumer;

class DataHolder extends RecyclerView.ViewHolder {
    private final TextView mTextContent;

    @SuppressLint("SetTextI18n")
    public DataHolder(@NonNull View itemView, final int viewType, final IntConsumer addCallback) {
        super(itemView);
        mTextContent = itemView.findViewById(R.id.item_content);

        Button addButton = itemView.findViewById(R.id.item_add);
        TextView textType = itemView.findViewById(R.id.item_type_text);
        ImageView icon = itemView.findViewById(R.id.icon);
        ImageView selector = itemView.findViewById(R.id.selector_view);
        switch (viewType) {
        case DataAdapter.VIEW_TYPE_BEFORE:
            textType.setText("Before Content");
            addButton.setOnClickListener(view -> addCallback.accept(viewType));
            break;
        case DataAdapter.VIEW_TYPE_AFTER:
            textType.setText("After Content");
            addButton.setOnClickListener(view -> addCallback.accept(viewType));
            break;
        case DataAdapter.VIEW_TYPE_INT:
            textType.setText("Card");
            addButton.setVisibility(View.GONE);
            break;
        default:
            textType.setText("<<< Unknown >>>");
            addButton.setVisibility(View.GONE);
            break;
        }

        ((ShadowImageView) icon).setShadowFromResId(R.drawable.legacy_thumb_up_hdpi_24);

        selector.setOnClickListener(v -> v.setSelected(!v.isSelected()));
        Shadowizer.shadowize(selector);
    }

    public void present(@Nullable Data data) {
        mTextContent.setText(data == null ? "NULL" : Long.toString(data.getValue()));
    }
}
