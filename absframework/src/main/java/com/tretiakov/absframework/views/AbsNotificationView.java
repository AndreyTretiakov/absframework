package com.tretiakov.absframework.views;

import android.content.Context;
import androidx.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.tretiakov.absframework.R;
import com.tretiakov.absframework.views.text.AbsTextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author andrewtretiakov; 7/28/18.
 */
public class AbsNotificationView extends RelativeLayout {

    public static final int INFO = 0, WARNING = 1, ERROR = 2, NONE = 3;
    @IntDef({INFO, WARNING, ERROR, NONE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Mode {}

    private int mMode;
    private String mTitle, mSubTitle;

    private AbsTextView mTitleTextView, mSubTitleTextView;

    public AbsNotificationView(Context context) {
        super(context);
    }

    public AbsNotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsNotificationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mTitleTextView = findViewById(android.R.id.text1);
        mSubTitleTextView = findViewById(android.R.id.text2);
    }

    public void build(@Mode int mode, String title, String subTitle) {
        this.build(mode, title, subTitle, null);
    }

    public void build(@Mode int mode, String title, String subTitle, View.OnClickListener cl) {
        mMode = mode;
        mTitle = title;
        mSubTitle = subTitle;
        setOnClickListener(cl);
        update();
    }

    public void destroy() {
        mMode = NONE;
        update();
    }

    private void update() {

        if (mMode == NONE) {
            setVisibility(GONE);
            return;
        }

        init();
        setIcon();
        setContent();
        setVisibility(VISIBLE);

        switch (mMode) {

            case INFO:
                setBackgroundResource(R.color.abs_notification_background_info);
                break;
            case WARNING:
                setBackgroundResource(R.color.abs_notification_background_warn);
                break;
            case ERROR:
                setBackgroundResource(R.color.abs_notification_background_error);
                break;
        }
    }

    private void setIcon() {
        int res = 0;
        switch (mMode) {

            case INFO:
                res = R.drawable.ic_action_info;
                break;
            case WARNING:
                res = R.drawable.ic_action_warning;
                break;
            case ERROR:
                res = R.drawable.ic_action_error;
                break;
        }

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mTitleTextView.setCompoundDrawablesWithIntrinsicBounds(res,0,0,0);
        } else {
            mTitleTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(res,0,0,0);
        }
    }

    private void setContent() {
        mTitleTextView.setText(mTitle);
        mSubTitleTextView.setText(mSubTitle);
    }
}
