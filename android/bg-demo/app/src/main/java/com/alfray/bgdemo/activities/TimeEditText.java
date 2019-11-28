package com.alfray.bgdemo.activities;

import android.content.Context;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatEditText;

public class TimeEditText extends AppCompatEditText {
    public TimeEditText(Context context) {
        super(context);
    }

    public TimeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        setSelected(false);
        setSelection(this.length());
        callOnClick();
    }

    @Override
    protected boolean getDefaultEditable() {
        return false;
    }

    @Override
    protected MovementMethod getDefaultMovementMethod() {
        return null;
    }
}
