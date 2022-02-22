package com.tretiakov.absframework.abs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;

import android.widget.TextView;

import com.tretiakov.absframework.R;
import com.tretiakov.absframework.constants.AbsConstants;
import com.tretiakov.absframework.context.AbsContext;
import com.tretiakov.absframework.routers.AbsCallback;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
@SuppressWarnings("unchecked")
public abstract class AbsFragment extends Fragment implements AbsConstants {

//    final String LOG_TAG = AbsFragment.class.getSimpleName();

    private AbsActivity mActivity;

    private AbsCallback mAbsCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AbsActivity) context;
    }

    @Nullable
    public AbsActivity requireAbsActivity() {
        return mActivity;
    }

    public static AbsFragment instance(Class<? extends AbsFragment> fClass, Bundle bundle, AbsCallback absCallback) {
        AbsFragment f = (AbsFragment) instantiate(AbsContext.getInstance().getContext(), fClass.getName());
        f.setArguments(bundle);
        f.setCallback(absCallback);
        return f;
    }

    protected AbsFragment instanceFragment(Bundle bundle, AbsCallback router) {
        setArguments(bundle);
        setCallback(router);
        return this;
    }

    protected AbsFragment instanceFragment(AbsCallback router) {
        setCallback(router);
        return this;
    }

    public void showUnCancelableDialog(Class dialog, Bundle bundle, AbsCallback absCallback) {
        if (isVisible() && mActivity != null) {
            mActivity.showUnCancelableDialog(dialog, bundle, absCallback);
        }
    }

    protected void requestPermission(@NonNull AbsCallback<Bundle> router, String... permissions) {
        mActivity.requestPermission(router, permissions);
    }

    public void setCallback(AbsCallback router) {
        mAbsCallback = router;
    }

    protected void switchActivity(@NonNull Class activity, @Nullable Bundle bundle,
                                                        int request, @Nullable AbsCallback router) {
        if (mActivity != null) {
            mActivity.switchActivity(activity, bundle, request, router);
        }
    }

    protected void startActivityAnClearStack(Class newActivity) {
        if (mActivity != null) {
            mActivity.startActivityAndClearStack(newActivity);
        }
    }

    protected void showDialog(Class dialog, Bundle bundle, AbsCallback absCallback) {
        if (getContext() == null) return;

        AbsDialog d = (AbsDialog) AbsDialog.instantiate(getContext(), dialog.getName(), bundle);
        if (absCallback != null) d.setCallback(absCallback);
        d.show(((AbsActivity) getContext()).getSupportFragmentManager(), dialog.getName());
    }

    protected void showAlertDialog(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }

    protected void showFragment(Class fragment, Bundle bundle, Boolean addToBackStack, AbsCallback absCallback) {
        showFragment(fragment, bundle, addToBackStack, R.id.fragment, absCallback);
    }

    protected void showFragment(Class fragment, Bundle bundle, Boolean addToBackStack, int id, AbsCallback absCallback) {
        if (mActivity != null) mActivity.showFragment(fragment, bundle, addToBackStack, id, absCallback);
    }

    protected void addFragment(Class fragment, Bundle bundle, Boolean addToBackStack, AbsCallback absCallback) {
        addFragment(fragment, bundle, addToBackStack, R.id.fragment, absCallback);
    }

    protected void addKotlinFragment(Fragment fragment, Bundle bundle, Boolean addToBackStack, AbsCallback<?> absCallback) {
        addKotlinFragment(fragment, bundle, addToBackStack, R.id.fragment, absCallback);
    }

    protected void addFragment(Class fragment, Bundle bundle, Boolean addToBackStack, int id, AbsCallback absCallback) {
        if (mActivity != null) mActivity.addFragment(fragment, bundle, addToBackStack, id, absCallback);
    }

    protected void addKotlinFragment(Fragment fragment, Bundle bundle, Boolean addToBackStack, int id, AbsCallback absCallback) {
        if (mActivity != null) mActivity.addKotlinFragment(fragment, bundle, addToBackStack, id, absCallback);
    }

    protected void addFragmentRTL(Class fragment, Bundle bundle, Boolean addToBackStack, int id, AbsCallback absCallback) {
        if (mActivity != null) mActivity.addFragmentRTL(fragment, bundle, addToBackStack, id, absCallback);
    }

    protected void addFragmentRTL(Class fragment, Bundle bundle, Boolean addToBackStack, AbsCallback absCallback) {
        addFragmentRTL(fragment, bundle, addToBackStack, R.id.fragment, absCallback);
    }

    protected <T> void onData(@Nullable T data, boolean needBack) {
        if (mAbsCallback != null) {
            mAbsCallback.result(data);
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

    protected void sendBroadcast(Intent intent) {
        if (getContext() == null) return;
        getContext().getApplicationContext().sendBroadcast(intent);
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

//    protected void setStatusBarColor(int color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getActivity() != null) {
//
//            Window window = getActivity().getWindow();
//
//            // clear FLAG_TRANSLUCENT_STATUS flag:
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//            // finally change the color
//            window.setStatusBarColor(ContextCompat.getColor(getActivity(), color));
//        }
//    }

//    protected void setStatusBarAndNavigationColor(int color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getActivity() != null) {
//            Window window = getActivity().getWindow();
//            setStatusBarColor(color);
//            window.setNavigationBarColor(ContextCompat.getColor(getActivity(),
//                    color == R.color.abs_color_status_bar ? R.color.abs_colorPrimary_V3 : color));
//        }
//    }

//    protected void setStatusBarDefaultColor() {
//        setStatusBarColor(isPre23() ? R.color.abs_colorPrimaryDark : R.color.abs_color_status_bar);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getActivity() != null) {
//            Window window = getActivity().getWindow();
//            window.setNavigationBarColor(ContextCompat.getColor(getActivity(), R.color.abs_color_status_bar));
//        }
//    }

    protected Context getAppContext() {
        return getContext().getApplicationContext();
    }
}
