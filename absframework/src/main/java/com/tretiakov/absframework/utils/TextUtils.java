package com.tretiakov.absframework.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Andrey Tretiakov 23.11.2014.
 */
public class TextUtils {

    @NonNull
    public static String firstCharToUp(@Nullable String s) {
        return s != null && !s.equals("") ? Character.toUpperCase(s.charAt(0)) + s.substring(1) : "";
    }

    @NonNull
    public static String formatSpaces(@Nullable String rawTitle) {
        if (rawTitle == null)
            return "";

        rawTitle = rawTitle.replaceAll("\n+", "\n");
        if (rawTitle.substring(rawTitle.length() - 1, rawTitle.length()).equals("\n"))
            rawTitle = rawTitle.substring(0, rawTitle.length() - 1);
        return rawTitle;
    }
}
