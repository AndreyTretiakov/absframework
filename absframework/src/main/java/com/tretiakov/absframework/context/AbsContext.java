package com.tretiakov.absframework.context;

import android.content.Context;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
public class AbsContext {

    private static AbsContext mInstance;

    private Context mContext;

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

}
