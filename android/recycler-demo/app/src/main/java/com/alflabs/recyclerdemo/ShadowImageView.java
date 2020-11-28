/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alflabs.recyclerdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.DrawableRes;

/**
 * Custom Image View Test
 */
@SuppressLint("AppCompatCustomView")
public class ShadowImageView extends ImageView {

    public ShadowImageView(Context context) {
        this(context, null);
    }

    public ShadowImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setShadowFromResId(@DrawableRes int iconId) {
        Log.d("@@", "setShadowFromResId " + iconId);
        Drawable drawable = getContext().getDrawable(iconId);
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Log.d("@@", "Bounds: " + w + "x" + h);
        drawable.setBounds(0,0, w, h);

        Bitmap icon = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(icon);
        drawable.draw(canvas);

        final int sz = 4;
        Bitmap shadow = Bitmap.createBitmap(w + 2*sz, h + 2*sz, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(shadow);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0x40FFFFFF);
        for (int i = sz, a = 0x20 ; i > 0; i--, a += 0x20) {
            paint.setColorFilter(new LightingColorFilter(0, 0x00202020));
            canvas.drawBitmap(icon, i * 2, i * 2, paint);
        }
        canvas.drawBitmap(icon, 0, 0, null);
        icon.recycle();

        setImageBitmap(shadow);
    }
}
