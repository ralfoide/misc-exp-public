package com.alflabs.testverifynewapicompat;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.TransitionManager;
import android.view.ViewGroup;

public class ExternalClassUsingTransitionManager {
    @TargetApi(Build.VERSION_CODES.M)
    public void doSomething(ViewGroup group) {
        TransitionManager.endTransitions(group);
    }
}
