package com.alflabs.recyclerdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

class Shadowizer {
    private static Drawable shadowizeBmpDrawable(@NonNull Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        drawable.setBounds(0, 0, w, h);

        Bitmap icon = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(icon);
        drawable.draw(canvas);

        final int sz = 4;
        Bitmap shadow = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(shadow);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0x40FFFFFF);
        for (int i = sz; i > 0; i--) {
            paint.setColorFilter(new LightingColorFilter(0, 0x00202020));
            canvas.drawBitmap(icon, i, i, paint);
        }
        canvas.drawBitmap(icon, 0, 0, null);
        icon.recycle();

        Drawable dest = new BitmapDrawable(shadow);
        dest.setBounds(0, 0, shadow.getWidth(), shadow.getHeight());
        return dest;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static Drawable shadowizeListDrawable(@NonNull StateListDrawable in) {
        StateListDrawable out = new StateListDrawable();
        int n = in.getStateCount(); // API 29 only
        for(int i = 0; i < n; i++) {
            Drawable d = shadowizeBmpDrawable(in.getStateDrawable(i));
            int[] st = in.getStateSet(i);
            out.addState(st, d);
        }

        return out;
    }

    private static Drawable shadowizeDrawable(@NonNull Drawable drawable) {
        if (drawable instanceof StateListDrawable) {
            return shadowizeListDrawable((StateListDrawable) drawable);
        } else {
            return shadowizeBmpDrawable(drawable);
        }
    }

    public static void shadowize(@Nullable View view) {
        if (view instanceof ImageView) {
            ImageView iv = (ImageView) view;
            Drawable d = iv.getDrawable();
            if (d != null) {
                iv.setImageDrawable(shadowizeDrawable(d));
            }
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            Drawable[] drawables = tv.getCompoundDrawablesRelative();
            if (drawables.length == 4) {
                boolean hasDrawables = false;
                for (int i = 0; i < drawables.length; i++) {
                    Drawable d = drawables[i];
                    if (d != null) {
                        hasDrawables = true;
                        drawables[i] = shadowizeDrawable(d);
                    }
                }
                if (hasDrawables) {
                    tv.setCompoundDrawablesRelative(drawables[0], drawables[1], drawables[2], drawables[3]);
                }
            }
        }
    }
}
