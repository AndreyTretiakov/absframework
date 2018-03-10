package com.tretiakov.absframework.abs;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tretiakov.absframework.constants.AbsConstants;
import com.tretiakov.absframework.routers.Callback;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
public class AbsDialog<T> extends DialogFragment implements AbsConstants {

    private boolean mIsVisible;

    private Callback<T> mRouter;

    private Handler mHandler = new Handler();

    public void setCallback(@NonNull Callback<T> callback) {
        mRouter = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mIsVisible = false;
        super.onSaveInstanceState(outState);
    }

    protected void setCustomBackground(@DrawableRes int background) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(background);
        }
    }

    protected void setCustomWidth(View view, int width) {
        Point point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(point);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) (point.x - getContext().getResources().getDimension(width) * 2);
        view.setLayoutParams(params);
    }

    public void onData(@Nullable T data) {
        if (mRouter != null) {
            mRouter.result(data);
        }

        close();
    }

    public void onData(@Nullable T data, boolean needDismiss) {
        if (mRouter != null) {
            mRouter.result(data);
        }

        if (needDismiss) {
            close();
        }
    }

    public void onDataAllowingStateLoss(@Nullable T data) {
        if (mRouter != null) {
            mRouter.result(data);
        }

        dismissAllowingStateLoss();
    }

    private void close() {
        if (isVisible() || isVisibleLocal()) {
            dismiss();
        }
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

    protected int getDimen(@DimenRes int dimen) {
        return (int) getResources().getDimension(dimen);
    }

    @Override
    public void onStart() {
        super.onStart();
        mIsVisible = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        mIsVisible = false;
    }

    public boolean isVisibleLocal() {
        return mIsVisible;
    }

    protected Handler getHandler() {
        return mHandler;
    }

    protected int optColor(@ColorRes int color) {
        return ContextCompat.getColor(getContext(), color);
    }

    @NonNull
    protected String getAction(Bundle bundle) {
        if (bundle == null) {
            return "";
        }

        return bundle.getString("action", "");
    }
}
