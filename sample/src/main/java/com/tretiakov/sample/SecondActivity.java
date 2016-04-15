package com.tretiakov.sample;

import android.util.Log;

import com.tretiakov.absframework.abs.AbsActivity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
@SuppressWarnings("unchecked")
@EActivity(R.layout.activity_second)
public class SecondActivity extends AbsActivity {

    @AfterInject
    void create() {
        Log.d("TAG", "create");
    }

    @Click
    void back() {
        deliverResult("ok");
    }
}
