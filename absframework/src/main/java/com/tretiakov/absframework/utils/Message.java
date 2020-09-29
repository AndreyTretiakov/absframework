package com.tretiakov.absframework.utils;

import android.annotation.SuppressLint;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tretiakov.absframework.R;
import com.tretiakov.absframework.context.AbsContext;

/**
 * @author Andrey Tretiakov created on 01.02.2015.
 */
public class Message {

    public static void shortToast(String msg) {
        showToast(msg, null, Toast.LENGTH_SHORT);
    }

    public static void shortToast(@StringRes int msg) {
        showToast(AbsContext.getInstance().getContext().getString(msg), null, Toast.LENGTH_SHORT);
    }

    public static void longToast(@StringRes int msg) {
        showToast(AbsContext.getInstance().getContext().getString(msg), null, Toast.LENGTH_LONG);
    }

    public static void longToast(String msg) {
        showToast(msg, null, Toast.LENGTH_LONG);
    }

    public static void longToast(SpannableStringBuilder builder) {
        showToast(null, builder, Toast.LENGTH_LONG);
    }

    @SuppressLint("InflateParams")
    private static void showToast(@Nullable String msg, SpannableStringBuilder builder, int duration) {
        if (!AbsContext.hasActivities()) {
            return;
        }

        if (android.text.TextUtils.isEmpty(msg) && builder == null) {
            return;
        }

        View layout = LayoutInflater.from(AbsContext.getInstance().getContext())
                .inflate(R.layout.toast_layout, null);

        TextView text = (TextView) layout.findViewById(R.id.toastTitle);
        if (builder != null) {
            text.setText(builder);
        } else {
            text.setText(msg);
        }

        Toast toast = new Toast(AbsContext.getInstance().getContext());
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }
}
