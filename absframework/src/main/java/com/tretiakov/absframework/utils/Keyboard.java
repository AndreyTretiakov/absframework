package com.tretiakov.absframework.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;
import static android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT;

/**
 * @author Andrey Tretiakov created on 05.02.2015.
 */
public class Keyboard {

    public static void hide(Activity activity) {
        new Handler().postDelayed(() ->
                activity.getWindow().setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN), 200);
    }

    public static void hide(Context context, View view) {
        if (view instanceof ViewGroup) {
            recursiveLoopChildren(context.getSystemService(INPUT_METHOD_SERVICE), (ViewGroup) view);
        } else {
            if (view instanceof EditText) {
                ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(view.getWindowToken(), HIDE_NOT_ALWAYS);
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
                    ((InputMethodManager) imm).hideSoftInputFromWindow(child.getWindowToken(),
                            HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    public static void show(Activity activity) {
        new Handler().postDelayed(() ->
                activity.getWindow().setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_VISIBLE), 200);
    }

    public static void show(Context context, EditText input) {
        new Handler().post(() -> ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE))
                .showSoftInput(input, SHOW_IMPLICIT));
    }
}
