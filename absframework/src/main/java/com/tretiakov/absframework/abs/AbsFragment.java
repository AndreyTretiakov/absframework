package com.tretiakov.absframework.abs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tretiakov.absframework.R;
import com.tretiakov.absframework.constants.AbsConstants;
import com.tretiakov.absframework.context.AbsContext;
import com.tretiakov.absframework.routers.Callback;

import java.util.HashMap;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
@SuppressWarnings("unchecked")
public abstract class AbsFragment<T> extends Fragment implements AbsConstants {

//    final String LOG_TAG = AbsFragment.class.getSimpleName();

    private AbsActivity mActivity;

    private Callback<T> mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AbsActivity) context;
    }

    public static AbsFragment instance(Class<? extends AbsFragment> fClass, Bundle bundle, Callback callback) {
        AbsFragment f = (AbsFragment) instantiate(AbsContext.getInstance().getContext(), fClass.getName());
        f.setArguments(bundle);
        f.setCallback(callback);
        return f;
    }

    protected AbsFragment<T> instanceFragment(Callback router) {
        setCallback(router);
        return this;
    }

    protected void requestPermission(Callback<Bundle> router, String... permissions) {
        mActivity.requestPermission(router, permissions);
    }

    public void setCallback(Callback<T> router) {
        mCallback = router;
    }

    protected void switchActivity(@NonNull Class activity, @Nullable Bundle bundle,
                                                        int request, @Nullable Callback<T> router) {
        if (mActivity != null) {
            mActivity.switchActivity(activity, bundle, request, router);
        }
    }

    protected void startActivityAnClearStack(Class newActivity) {
        if (mActivity != null) {
            mActivity.startActivityAndClearStack(newActivity);
        }
    }

    protected AbsDialog showDialog(Class dialog, Bundle bundle, Callback<T> callback) {
        if (isVisible()) {
            AbsDialog d = (AbsDialog) AbsDialog.instantiate(getContext(), dialog.getName(), bundle);
            if (callback != null) d.setCallback(callback);
            if (mActivity != null && isVisible()) {
                try {
                    d.show(getChildFragmentManager(), dialog.getName());
                    return d;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        return null;
    }

    protected void showAlertDialog(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }

    protected void showFragment(Class fragment, Bundle bundle, Boolean addToBackStack, Callback<T> callback) {
        showFragment(fragment, bundle, addToBackStack, R.id.fragment, callback);
    }

    protected void showFragment(Class fragment, Bundle bundle, Boolean addToBackStack, int id, Callback<T> callback) {
        if (mActivity != null) mActivity.showFragment(fragment, bundle, addToBackStack, id, callback);
    }

    protected void addFragment(Class fragment, Bundle bundle, Boolean addToBackStack, Callback<T> callback) {
        addFragment(fragment, bundle, addToBackStack, R.id.fragment, callback);
    }

    protected void addFragment(Class fragment, Bundle bundle, Boolean addToBackStack, int id, Callback<T> callback) {
        if (mActivity != null) mActivity.addFragment(fragment, bundle, addToBackStack, id, callback);
    }

    protected String getStringBundle(String key) {
        if (getArguments() != null) {
            return getArguments().getString(key);
        } else {
            return null;
        }
    }

    protected T getParcelableBundle(String key) {
        if (getArguments() != null) {
            return getArguments().getParcelable(key);
        } else {
            return null;
        }
    }

    protected void onData(@Nullable T data, boolean needBack) {
        if (mCallback != null) {
            mCallback.result(data);
        }

        if (needBack) {
            onBackPressed();
        }
    }

    protected void onBackPressed() {
        if (mActivity != null && isVisible()) {
            try {
                mActivity.onBackPressed();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    protected String getAction(Bundle bundle) {
        if (bundle == null) {
            return "";
        }

        return bundle.getString("action", "");
    }

    public int optColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    protected void sendLocalBroadcast(Intent intent) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    protected void registerLocalBroadcast(BroadcastReceiver receiver, String... actions) {
        IntentFilter filter = new IntentFilter();
        for (String action : actions) {
            filter.addAction(action);
        }

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
    }

    protected void unregisterLocalBroadcast(BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    protected void runOnUiThread(Runnable action) {
        getActivity().runOnUiThread(action);
    }

    protected void addDrawableToLeft(TextView view, int res) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            view.setCompoundDrawablesWithIntrinsicBounds(res,0,0,0);
        } else {
            view.setCompoundDrawablesRelativeWithIntrinsicBounds(res,0,0,0);
        }
    }

    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(getContext(), color));
        }
    }

    protected Context getAppContext() {
        return getContext().getApplicationContext();
    }
}
