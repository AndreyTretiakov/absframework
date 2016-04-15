package com.tretiakov.absframework.views.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.tretiakov.absframework.R;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
public class TextView extends android.widget.TextView {

    public TextView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public TextView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(@NonNull Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Font);
        String font = a.getString(R.styleable.Font_font);
        a.recycle();

        setTypeface(FontsHelper.getTypeFace(getContext(), "fonts/" +
                (font == null ? "Roboto-Regular" : font) + ".ttf"));
    }
}
