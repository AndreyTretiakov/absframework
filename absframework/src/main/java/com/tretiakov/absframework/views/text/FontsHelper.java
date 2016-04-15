package com.tretiakov.absframework.views.text;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * @author Andrey Tretiakov
 */
public class FontsHelper {

    private static Hashtable<String, Typeface> sTypeFaces = new Hashtable<>(4);

    public static Typeface getTypeFace(Context context, String fileName) {
        Typeface tempTypeface = sTypeFaces.get(fileName);

        if (tempTypeface == null) {
            tempTypeface = Typeface.createFromAsset(context.getAssets(), fileName);
            sTypeFaces.put(fileName, tempTypeface);
        }

        return tempTypeface;
    }

}
