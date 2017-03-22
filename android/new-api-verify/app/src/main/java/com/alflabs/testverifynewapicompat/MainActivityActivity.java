package com.alflabs.testverifynewapicompat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivityActivity extends Activity {

    private TextView mTextView;
    private ViewGroup mViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mViewGroup = (ViewGroup) findViewById(R.id.group);
        mTextView = (TextView) findViewById(R.id.text);

        useExternalClass();
        useInnerClass();
    }

    private void useExternalClass() {
        try {
            ExternalClassUsingTransitionManager c = new ExternalClassUsingTransitionManager();
            addText("E: " + c.toString());
            c.doSomething(mViewGroup);
            addText("E: doSomething called.");
        } catch (Throwable t) {
            addText("E: " + t.getClass().getCanonicalName() + " "  + t.getMessage());
        }
    }

    private void useInnerClass() {
        try {
            InnerClassUsingTransitionManager c = new InnerClassUsingTransitionManager();
            addText("I: " + c.toString());
            c.doSomething(mViewGroup);
            addText("I: doSomething called.");
        } catch (Throwable t) {
            addText("I: " + t.getClass().getCanonicalName() + " "  + t.getMessage());
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

}
