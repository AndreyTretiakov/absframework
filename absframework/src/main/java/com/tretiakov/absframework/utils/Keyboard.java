package com.tretiakov.absframework.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;
import static android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT;

/**
 * @author Andrey Tretiakov created on 05.02.2015.
 */
public class Keyboard {

    public static void ADJUST_RESIZE(Activity activity) {
        setMode(activity, SOFT_INPUT_STATE_ALWAYS_HIDDEN|SOFT_INPUT_ADJUST_RESIZE);
    }

    public static void ADJUST_NOTHING(Activity activity) {
        setMode(activity, SOFT_INPUT_ADJUST_NOTHING);
    }

    public static void STATE_HIDDEN(Fragment fragment) {
        STATE_HIDDEN(fragment.getActivity());
    }

    public static void STATE_HIDDEN(Activity activity) {
        setMode(activity, SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void hide(Context context, View view) {
        if (context == null || view == null) return;

        if (view instanceof ViewGroup) {
            recursiveLoopChildren(context.getSystemService(INPUT_METHOD_SERVICE),
                    (ViewGroup) view);
        } else {
            if (view instanceof EditText) {
                InputMethodManager manager = (InputMethodManager)
                        context.getSystemService(INPUT_METHOD_SERVICE);
                if (manager != null) {
                    manager.hideSoftInputFromWindow(view.getWindowToken(), HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    private static void recursiveLoopChildren(Object imm, ViewGroup group) {
        for (int i = group.getChildCount() - 1; i >= 0; i--) {
            final View child = group.getChildAt(i);
            if (child instanceof ViewGroup) {
                recursiveLoopChildren(imm, (ViewGroup) child);
            } else {
                if (child instanceof EditText) {
                    ((InputMethodManager) imm).hideSoftInputFromWindow(
                            child.getWindowToken(), HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    public static void show(Activity activity) {
        setMode(activity, SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public static void show(Context context, EditText input) {
        if (context == null || input == null) return;

        new Handler().post(() -> {
            InputMethodManager manager = (InputMethodManager)
                    context.getSystemService(INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.showSoftInput(input, SHOW_IMPLICIT);
            }
        });
    }

    private static void setMode(Activity activity, int mode) {
        if (activity == null) {
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.getWindow().setSoftInputMode(mode);
            }
        }, 200);
    }
}