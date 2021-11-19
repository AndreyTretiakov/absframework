package com.tretiakov.absframework.context;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
public class AbsContext {

    private static Set<String> mActiveActivities = new HashSet<>();

    @SuppressLint("StaticFieldLeak")
    private static AbsContext mInstance;

    private final Context mContext;

    private AbsContext(Context context) {
        mContext = context;
    }

    public static void initialize(Context context) {
        if (mInstance == null) {
            mInstance = new AbsContext(context);
        }
    }

    public static AbsContext getInstance() {
        if (mInstance == null) {
            throw new RuntimeException("Initialize Abs Context in Application class");
        }

        return mInstance;
    }

    public Context getContext() {
        return mContext;
    }

    public static void addActivity(String activityName) {
        mActiveActivities.add(activityName);
    }

    public static void removeActivity(String activityName) {
        mActiveActivities.remove(activityName);
    }

    public static boolean hasActivities() {
        return !mActiveActivities.isEmpty();
    }

}
