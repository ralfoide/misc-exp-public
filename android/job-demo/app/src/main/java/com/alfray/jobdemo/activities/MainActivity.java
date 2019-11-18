package com.alfray.jobdemo.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.alfray.jobdemo.R;
import com.alfray.jobdemo.app.MainApplication;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainApplication.getMainAppComponent(this).inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
