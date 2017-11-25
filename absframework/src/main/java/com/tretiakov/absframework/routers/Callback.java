package com.tretiakov.absframework.routers;

import android.support.annotation.Nullable;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
public interface Callback<T> {

    void result(@Nullable T data);

}
