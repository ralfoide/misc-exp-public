package com.alflabs.testverifynewapicompat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivityActivity extends Activity {

    private TextView mTextView;
    private ViewGroup mViewGroup;

    public MainActivityActivity() {
        Log.d("MainActivityActivity", "Class Loaded");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mViewGroup = (ViewGroup) findViewById(R.id.group);
        mTextView = (TextView) findViewById(R.id.text);

        useExternalClass();
        useInnerClass();
        useMethod();
    }

    private void useExternalClass() {
        try {
            addText("\n-- Use External Class --");
            ExternalClassUsingTransitionManager c = new ExternalClassUsingTransitionManager();
            addText("E: instance created: " + c.toString());
            c.doSomething(mViewGroup);
            addText("E: doSomething called.");
        } catch (Throwable t) {
            addText("E: FAILED with " + t.getClass().getCanonicalName() + " "  + t.getMessage());
        }
    }

    private void useInnerClass() {
        try {
            addText("\n-- Use Inner Class --");
            InnerClassUsingTransitionManager c = new InnerClassUsingTransitionManager();
            addText("I: instance created: " + c.toString());
            c.doSomething(mViewGroup);
            addText("I: doSomething called.");
        } catch (Throwable t) {
            addText("I: FAILED with " + t.getClass().getCanonicalName() + " "  + t.getMessage());
        }
    }

    private void useMethod() {
        try {
            addText("\n-- Use Method --");
            useTransitionManagerDirectlyInMethod();
            addText("M: doSomething called.");
        } catch (Throwable t) {
            addText("M: FAILED with " + t.getClass().getCanonicalName() + " "  + t.getMessage());
        }
    }

    private void addText(String s) {
        mTextView.append("\n" + s);
    }


    public static class InnerClassUsingTransitionManager {
        @TargetApi(Build.VERSION_CODES.M)
        public void doSomething(ViewGroup group) {
            TransitionManager.endTransitions(group);
        }
    }

    /*
    Note that Dalvik shows this in LogCat when *this* class is loaded:
     .../com.alflabs.testverifynewapicompat I/dalvikvm: Could not find method android.transition.TransitionManager.endTransitions, referenced from method com.alflabs.testverifynewapicompat.MainActivityActivity.useTransitionManagerDirectlyInMethod
     .../com.alflabs.testverifynewapicompat W/dalvikvm: VFY: unable to resolve static method 661: Landroid/transition/TransitionManager;.endTransitions (Landroid/view/ViewGroup;)V
     .../com.alflabs.testverifynewapicompat D/dalvikvm: VFY: replacing opcode 0x71 at 0x0002

     So it does load *this* class and realize it accesses a class not available and replaces opcodes by no-ops.
     But the class still loads and is usable.
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void useTransitionManagerDirectlyInMethod() {
        TransitionManager.endTransitions(mViewGroup);
    }

}
