package com.tretiakov.absframework.views.text;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import android.hardware.input.InputManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.tretiakov.absframework.R;
import com.tretiakov.absframework.utils.Keyboard;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
public class AbsEditText extends AppCompatEditText {

    private String pattern;

    public interface OnSimpleTextChangeListener {
        void onTextChanged(String text);
    }

    public interface OnAdapterTextChangeListener {
        void onTextChanged(AbsEditText view, String text);
    }

    private OnAdapterTextChangeListener mAdapterListener;

    public AbsEditText(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public AbsEditText(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(@NonNull Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsFont);
            String font = a.getString(R.styleable.AbsFont__font);
            a.recycle();

            setTypeface(FontsHelper.getTypeFace(getContext(), "fonts/" +
                    (font == null ? "Roboto-Regular" : font) + ".ttf"));
//        setOnTouchListener(onTouch);
//        setOnEditorActionListener(onEdit);
        }
    }

    public boolean isValidEmail() {
        return (!TextUtils.isEmpty(text()) && Patterns.EMAIL_ADDRESS.matcher(text()).matches());
    }

//    fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(name.text()).matches()

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

    public void setPattern(String pattern) {
        this.pattern = pattern;
        setFilters(new InputFilter[] {filter});
    }

    public String text() {
        return getText().toString();
    }

    public void clearText() {
        setText(null);
    }

    public void setCursorToEnt() {
        if (!isEmpty()) {
            postDelayed(() -> setSelection(getText().length()), 1000);
        }
    }

    public void setTextBlockListener(String text) {
        removeTextChangedListener(mAdapterWatcher);
        super.setText(text);
        addTextChangedListener(mAdapterWatcher);
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(getText());
    }

    public void setSelectableMode(View.OnClickListener cl) {
        setOnClickListener(cl);
    }

    public void showKeyboard() {
        postDelayed(() -> Keyboard.show(getContext(), this), 200);
    }

    public void showKeyboardNow() {
        InputMethodManager manager = (InputMethodManager)
                getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
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

    public void setOnAdapterTextChangeListener(@NonNull OnAdapterTextChangeListener listener) {
        mAdapterListener = listener;
        addTextChangedListener(mAdapterWatcher);
    }

    private TextWatcher mAdapterWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mAdapterListener.onTextChanged(AbsEditText.this, s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            if (pattern != null && !source.toString().matches(pattern) && !source.toString().startsWith(".")) {
                return "";
            }

            return null;
        }
    };
}
