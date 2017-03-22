package com.alflabs.testverifynewapicompat;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.ViewGroup;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class ExternalClassUsingTransitionManager {

    Transition t = new AutoTransition();

    @TargetApi(Build.VERSION_CODES.M)
    public void doSomething(ViewGroup group) {
        TransitionManager.endTransitions(group);
    }
}
