package com.tretiakov.absframework.abs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.tretiakov.absframework.R;
import com.tretiakov.absframework.constants.AbsConstants;
import com.tretiakov.absframework.routers.IRouter;

import java.util.HashMap;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
@SuppressWarnings("unchecked")
public abstract class AbsFragment<T> extends Fragment implements AbsConstants {

    private AbsActivity mActivity;

    private IRouter<T> mRouter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AbsActivity) context;
    }

    public void setCallback(@NonNull IRouter<T> router) {
        mRouter = router;
    }

    protected <K extends String, V> void switchActivity(@NonNull Class activity, @Nullable HashMap<K, V> map,
                                                        int request, @Nullable IRouter<T> router) {
        if (mActivity != null) mActivity.switchActivity(activity, map, request, router);
    }

    protected void startActivityAnClearStack(Class newActivity) {
        if (mActivity != null) mActivity.startActivityAndClearStack(newActivity);
    }

    protected void showDialog(Class dialog, Bundle bundle, IRouter<T> callback) {
        AbsDialog d = (AbsDialog) AbsDialog.instantiate(getContext(), dialog.getName(), bundle);
        if (callback != null) d.setCallback(callback);
        d.show(getChildFragmentManager(), dialog.getName());
    }

    protected void showAlertDialog(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }

    protected void showFragment(Class fragment, IRouter<T> callback) {
        showFragment(fragment, Bundle.EMPTY, true, R.id.fragment, callback);
    }

    protected void showFragment(Class fragment, Bundle bundle, IRouter<T> callback) {
        showFragment(fragment, bundle, true, R.id.fragment, callback);
    }

    protected void showFragment(Class fragment, Bundle bundle, Boolean addToBackStack) {
        showFragment(fragment, bundle, addToBackStack, R.id.fragment, null);
    }

    protected void showFragment(Class fragment, Bundle bundle, Boolean addToBackStack, IRouter<T> callback) {
        showFragment(fragment, bundle, addToBackStack, R.id.fragment, callback);
    }

    protected void showFragment(Class fragment, Bundle bundle, int id, Boolean addToBackStack) {
        showFragment(fragment, bundle, addToBackStack, id, null);
    }

    protected void showFragment(Class fragment, Bundle bundle, Boolean addToBackStack, int id, IRouter<T> callback) {
        if (mActivity != null) mActivity.showFragment(fragment, bundle, addToBackStack, id, callback);
    }

    protected void onData(@Nullable T data, boolean needBack) {
        if (mRouter != null) {
            mRouter.onData(data);
        }

        if (needBack)
            onBackPressed();
    }

    protected void onBackPressed() {
        if (mActivity != null)
            mActivity.onBackPressed();
    }

}
