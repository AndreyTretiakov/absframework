package com.tretiakov.absframework.abs;

import com.tretiakov.absframework.routers.Callback;

import java.util.WeakHashMap;

/**
 * @author andrewtretiakov; 11/24/17.
 */

public class CallbackManager <T extends Callback> {

    private WeakHashMap<String, T> callbacks = new WeakHashMap<>();

    private static final CallbackManager ourInstance = new CallbackManager();

    public static CallbackManager getInstance() {
        return ourInstance;
    }


    public void addCallback(T callback) {
        if (callback != null) {
            callbacks.put(callback.getClass().getName(), callback);
        }
    }

    public void removeCallback(T callback) {
        if (callback != null) {
            callbacks.remove(callback.getClass().getName());
        }
    }

    public T getCallback(String name) {
        if (name != null) {
            return callbacks.get(name);
        } else {
            return null;
        }
    }

}
