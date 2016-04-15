package com.tretiakov.absframework.views.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.tretiakov.absframework.R;
import com.tretiakov.absframework.utils.Keyboard;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
public class AbsEditText extends android.widget.EditText {

    public interface OnSimpleTextChangeListener {
        void onTextChanged(String text);
    }

    public AbsEditText(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public AbsEditText(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(@NonNull Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsFont);
        String font = a.getString(R.styleable.AbsFont_font);
        a.recycle();

        setTypeface(FontsHelper.getTypeFace(getContext(), "fonts/" +
                (font == null ? "Roboto-Regular" : font) + ".ttf"));
        setOnTouchListener(onTouch);
        setOnEditorActionListener(onEdit);

    }

    private final OnTouchListener onTouch = (v, event) -> {
        setFocusableInTouchMode(true);
        setCursorVisible(true);
        return false;
    };

    private final OnEditorActionListener onEdit = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_DONE)
            setCursorVisible(false);

        return false;
    };

    public void setSelectableMode(View.OnClickListener cl) {
        setOnClickListener(cl);
    }

    public void showKeyboard() {
        postDelayed(() -> Keyboard.show(getContext(), this), 200);
    }

    public void hideKeyboard() {
        Keyboard.hide(getContext(), this);
    }

    public void setOnSimpleTextChangeListener(@NonNull OnSimpleTextChangeListener listener) {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listener.onTextChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
