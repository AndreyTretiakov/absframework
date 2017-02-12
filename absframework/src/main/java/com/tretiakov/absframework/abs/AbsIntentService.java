package com.tretiakov.absframework.abs;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * @author andrewtretiakov; 2/10/17.
 */

public abstract class AbsIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AbsIntentService(String name) {
        super(name);
    }

    protected void registerLocalReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    protected void unregisterLocalReceiver(BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
