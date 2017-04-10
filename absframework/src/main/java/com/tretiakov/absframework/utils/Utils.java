package com.tretiakov.absframework.utils;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author andrewtretiakov; 3/21/17.
 */

public class Utils {

    public static List emptyList() {
        return new ArrayList();
    }

    public static Object[] toArray(Object... objects) {
        return objects;
    }

    public static String userAgent_(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int densityDpi = (int) (metrics.density * 160f);
        int displayHeight = metrics.heightPixels;
        int displayWidth = metrics.widthPixels;

        return String.format("Android (%s/%s; %s; %s; %s; %s; %s; %s; %s_%s)",
                Build.VERSION.RELEASE,
                Build.VERSION.SDK,
                densityDpi + "dpi",
                displayHeight + "x" + displayWidth,
                Build.MODEL,
                Build.MANUFACTURER,
                Build.BRAND,
                Build.DEVICE,
                Locale.getDefault().getLanguage(),
                Locale.getDefault().getCountry()
        );
    }
}
