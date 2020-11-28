package com.alflabs.recyclerdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

class CustomLinearLayout extends LinearLayout {

    public CustomLinearLayout(Context context) {
        super(context);
        throw new RuntimeException("Stub!");
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void addView(View child) {
        super.addView(adjust(child));
    }

    @Override
    public void addView(View child, int index) {
        super.addView(adjust(child), index);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(adjust(child), width, height);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(adjust(child), params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(adjust(child), index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        return super.addViewInLayout(adjust(child), index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        return super.addViewInLayout(adjust(child), index, params, preventRequestLayout);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private View adjust(View original) {
        Log.d("@@", "Adjust " + original.getClass().getSimpleName());
        if (original instanceof TextView) {
            TextView tv = (TextView) original;
            tv.setShadowLayer(10, 4, 4, 0x80000000);
        }
        return original;
    }
}
