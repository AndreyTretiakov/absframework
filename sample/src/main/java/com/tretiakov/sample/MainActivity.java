package com.tretiakov.sample;

import android.util.Log;

import com.tretiakov.absframework.abs.AbsActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@SuppressWarnings("unchecked")
@EActivity(R.layout.activity_main)
public class MainActivity extends AbsActivity {

    @Click
    void show() {
        switchActivity(SecondActivity_.class, null, RESULT_REQUEST, data -> {
            Log.d("TAG", (String) data);
        });
    }
}
