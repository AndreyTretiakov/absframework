package com.tretiakov.absframework.views.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.tretiakov.absframework.R;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
public class AbsTextView extends AppCompatTextView {

    public AbsTextView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public AbsTextView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(@NonNull Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsFont);
            String font = a.getString(R.styleable.AbsFont_font);
            a.recycle();

            setTypeface(FontsHelper.getTypeFace(getContext(), "fonts/" +
                    (font == null ? "Roboto-Regular" : font) + ".ttf"));
        }
    }

    public void setMediumFont() {
        setTypeface(FontsHelper.getTypeFace(getContext(), "fonts/Roboto-Medium.ttf"));
    }

    public void setRegularFont() {
        setTypeface(FontsHelper.getTypeFace(getContext(), "fonts/Roboto-Regular.ttf"));
    }
}
