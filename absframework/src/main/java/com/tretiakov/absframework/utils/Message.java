package com.tretiakov.absframework.utils;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public static void shortToast(@StringRes int msg) {
        showToast(AbsContext.getInstance().getContext().getString(msg), Toast.LENGTH_SHORT);
    }

    public static void longToast(@StringRes int msg) {
        showToast(AbsContext.getInstance().getContext().getString(msg), Toast.LENGTH_LONG);
    }

    public static void longToast(String msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    @SuppressLint("InflateParams")
    private static void showToast(@Nullable String msg, int duration) {
        if (msg == null || msg.isEmpty())
            return;

        View layout = LayoutInflater.from(AbsContext.getInstance().getContext())
                .inflate(R.layout.toast_layout, null);

        ((TextView) layout.findViewById(R.id.toastTitle)).setText(msg);

        Toast toast = new Toast(AbsContext.getInstance().getContext());
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }
}
