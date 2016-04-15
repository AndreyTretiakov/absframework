package com.tretiakov.absframework.views;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import com.tretiakov.absframework.routers.OnItemPositionListener;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
public class AbsSpinner extends AppCompatSpinner {

    public AbsSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnSimpleItemListener(OnItemPositionListener listener) {
        setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}