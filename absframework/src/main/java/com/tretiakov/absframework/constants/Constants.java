package com.tretiakov.absframework.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
public interface Constants {

    int MODE_SEARCH_ALL = 1;
    int MODE_SEARCH_START = 2;
    @IntDef({MODE_SEARCH_ALL, MODE_SEARCH_START})
    @Retention(RetentionPolicy.SOURCE)
    @interface SearchMode {}

    int HEADER = 0;
    int ITEM = 1;
    int FOOTER = 2;
    @IntDef({HEADER, ITEM, FOOTER})
    @Retention(RetentionPolicy.SOURCE)
    @interface ItemType {}

    int NO_ID = -1;
    int NO_REQUEST = -1;
    int RESULT_REQUEST = 1;
    int PARAM_CLEAR_STACK = 2;
    int PARAM_CLEAR_TOP = 3;

    String KEY_DATA = "data";

}
