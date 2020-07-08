package com.tretiakov.absframework.views.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
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
public class AbsCheckButton extends AppCompatCheckBox {

    public AbsCheckButton(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public AbsCheckButton(@NonNull Context context, AttributeSet attrs) {
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

    public void setTitleSubtitle(CharSequence title, CharSequence subtitle) {
        setSubtitle(title, subtitle, null);
    }

    public void setSubtitle(CharSequence title, CharSequence subtitle, String font) {
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
