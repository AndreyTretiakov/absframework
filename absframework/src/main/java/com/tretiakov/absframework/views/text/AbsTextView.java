package com.tretiakov.absframework.views.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
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
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsFont);
            String font = a.getString(R.styleable.AbsFont_font);

            CharSequence title = a.getText(R.styleable.AbsFont__title);
            CharSequence subtitle = a.getText(R.styleable.AbsFont__subtitle);
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
