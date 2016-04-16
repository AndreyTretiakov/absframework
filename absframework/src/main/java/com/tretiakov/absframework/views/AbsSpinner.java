package com.tretiakov.absframework.views;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.tretiakov.absframework.constants.AbsConstants;
import com.tretiakov.absframework.routers.OnItemPositionListener;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
@SuppressWarnings("unchecked")
public class AbsSpinner extends AppCompatSpinner implements AbsConstants {

    public AbsSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDefaultAdpater(@ArrayRes int stringArray, @LayoutRes int item,
                                  @NonNull OnItemPositionListener listener) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter(getContext(),
                item == AbsConstants.NO_ID ? android.R.layout.simple_spinner_item : item,
                getResources().getStringArray(stringArray));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setAdapter(spinnerArrayAdapter);
        setOnSimpleItemListener(listener);
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