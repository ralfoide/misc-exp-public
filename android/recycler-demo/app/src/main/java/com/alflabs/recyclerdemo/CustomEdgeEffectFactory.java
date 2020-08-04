package com.alflabs.recyclerdemo;

import android.util.Log;
import android.view.View;
import android.widget.EdgeEffect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class CustomEdgeEffectFactory  extends RecyclerView.EdgeEffectFactory {
    private static final String TAG = "@@ " + CustomEdgeEffectFactory.class.getSimpleName();

    @NonNull
    @Override
    protected EdgeEffect createEdgeEffect(@NonNull RecyclerView view, int direction) {
        final int deltaCoef = direction == DIRECTION_TOP ? -1 : 1;
        return new EdgeEffect(view.getContext()) {
            /** A view should call this when content is pulled away from an edge by the user. */
            @Override
            public void onPull(float deltaDistance) {
                super.onPull(deltaDistance);
                applyTranslation(deltaDistance * deltaCoef);
            }

            /** A view should call this when content is pulled away from an edge by the user. */
            @Override
            public void onPull(float deltaDistance, float displacement) {
                super.onPull(deltaDistance, displacement);
                applyTranslation(deltaDistance * deltaCoef);
            }

            private void applyTranslation(float deltaY) {
                float y = deltaY * super.getMaxHeight();
                Log.d(TAG, "EdgeEffect Translate: " + deltaY + " => " + y + " // mh: " + super.getMaxHeight());
                for (int i = 0; i < view.getChildCount(); i++) {
                    View child = view.getChildAt(i);
                    RecyclerView.ViewHolder holder = view.getChildViewHolder(child);
                    holder.itemView.scrollBy(0, (int) y);
                }
            }

            /** Call when the object is released after being pulled. */
            @Override
            public void onRelease() {
                super.onRelease();
                Log.d(TAG, "EdgeEffect onRelease");
                for (int i = 0; i < view.getChildCount(); i++) {
                    View child = view.getChildAt(i);
                    RecyclerView.ViewHolder holder = view.getChildViewHolder(child);
                    holder.itemView.setScrollY(0);
                }
            }

            /** Call when the effect absorbs an impact at the given velocity. */
            @Override
            public void onAbsorb(int velocity) {
                super.onAbsorb(velocity);
                Log.d(TAG, "EdgeEffect onAbsorb velocity=" + velocity);
            }

            @Override
            public void setSize(int width, int height) {
                super.setSize(width, height);
                Log.d(TAG, "EdgeEffect setSize: " + width + " x " + height);
            }

            @Override
            public int getMaxHeight() {
                Log.d(TAG, "EdgeEffect getMaxHeight: " + super.getMaxHeight());
                return super.getMaxHeight();
            }
        };
    }
}
