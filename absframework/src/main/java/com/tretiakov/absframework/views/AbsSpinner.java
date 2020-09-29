package com.tretiakov.absframework.views;

import android.content.Context;
import androidx.annotation.ArrayRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
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

    public void setDefaultAdapter(@ArrayRes int stringArray, @LayoutRes int item,
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