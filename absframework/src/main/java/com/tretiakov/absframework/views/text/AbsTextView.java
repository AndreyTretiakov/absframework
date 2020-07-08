package com.tretiakov.absframework.views.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
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
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsTextView);
            String font = a.getString(R.styleable.AbsTextView__font);

            CharSequence title = a.getText(R.styleable.AbsTextView__title);
            CharSequence subtitle = a.getText(R.styleable.AbsTextView__subtitle);
            if (subtitle != null) {
                setSubtitle(title, subtitle, font);
            } else if (title != null){
                setText(title);
            }

            a.recycle();

            setTypeface(FontsHelper.getTypeFace(getContext(), "fonts/" +
                    (font == null ? "Roboto-Regular" : font) + ".ttf"));
        }
    }

    public void setRegularTypeface() {
        setTypeface(FontsHelper.getTypeFace(getContext(), "fonts/Roboto-Regular.ttf"));
    }

    public void setMediumTypeface(boolean isMedium) {
        setTypeface(FontsHelper.getTypeFace(getContext(), isMedium ? "fonts/Roboto-Medium.ttf" : "fonts/Roboto-Regular.ttf"));
    }

    public void setTitle(CharSequence title) {
        String[] parts = getText().toString().split("\n");
        parts[0] = title.toString();
        if (parts.length == 2) {
            setSubtitle(parts[0], parts[1], null);
        } else {
            setText(parts[0]);
        }
    }

    public void setSubtitle(CharSequence subtitle) {
        setSubtitle(getText(), subtitle, null);
    }

    public void setTitleSubtitle(CharSequence title, CharSequence subtitle) {
        setSubtitle(title, subtitle, null);
    }

    private void setSubtitle(CharSequence title, CharSequence subtitle, String font) {
        int subtitleStart;
        SpannableStringBuilder builder = new SpannableStringBuilder(title);

        if ("Roboto-Bold".equals(font)) {
            builder.setSpan(new StyleSpan(Typeface.BOLD), 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        builder.append("\n");
        subtitleStart = builder.length();
        builder.append(subtitle);
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.textSecondary)), subtitleStart, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new RelativeSizeSpan(0.7f), subtitleStart, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(builder);
    }
}
