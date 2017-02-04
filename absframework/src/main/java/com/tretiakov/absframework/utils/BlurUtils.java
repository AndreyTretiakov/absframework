package com.tretiakov.absframework.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * @author andrewtretiakov; 2/3/17.
 */

public class BlurUtils {

    public static Bitmap screenshot(Activity activity, boolean fade) {
        return screenshot(activity, null, fade);
    }


    private static Bitmap screenshot(Activity activity, Rect cropRect, boolean fade) {
        if (activity != null) {
            Bitmap bitmap = screenshot(activity, cropRect);

            if (fade && bitmap != null) {
                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#b2000000"));
                canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
            }

            return bitmap;
        }

        return null;
    }

    private static Bitmap screenshot(Activity activity, Rect cropRect) {
        Bitmap screenshot = null;

        if (activity != null) {
            View view = activity.getWindow().getDecorView().getRootView();
            view.setDrawingCacheEnabled(true);
            view.getMeasuredWidth();
            view.getMeasuredHeight();

            int bottomBar = 0;

            Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                bottomBar = resources.getDimensionPixelSize(resourceId);
            }

            Bitmap drawingCache = view.getDrawingCache();

            if (drawingCache != null) {
                if (cropRect != null) {
                    screenshot = Bitmap.createBitmap(drawingCache, 0, cropRect.top, drawingCache.getWidth(), drawingCache.getHeight() - cropRect.top - bottomBar, null, true);
                } else {
                    screenshot = Bitmap.createBitmap(drawingCache, 0, 0, drawingCache.getWidth(), drawingCache.getHeight(), null, true);
                }
            }
            view.setDrawingCacheEnabled(false);
        }

        return screenshot;
    }

}
