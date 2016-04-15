package com.tretiakov.absframework.abs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Window;

import com.tretiakov.absframework.R;
import com.tretiakov.absframework.routers.IRouter;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
public abstract class AbsDialog<T> extends DialogFragment {

    private IRouter<T> mRouter;

    public void setCallback(@NonNull IRouter<T> callback) {
        mRouter = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void onData(@Nullable T data) {
        if (mRouter != null) {
            mRouter.onData(data);
        }

        dismiss();
    }

    protected int getDimen(@DimenRes int dimen) {
        return (int) getResources().getDimension(dimen);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRouter = null;
    }
}
