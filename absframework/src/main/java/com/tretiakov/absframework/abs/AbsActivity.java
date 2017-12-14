package com.tretiakov.absframework.abs;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.tretiakov.absframework.R;
import com.tretiakov.absframework.constants.AbsConstants;
import com.tretiakov.absframework.routers.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
@SuppressWarnings("unchecked")
public abstract class AbsActivity<T> extends AppCompatActivity implements AbsConstants {

    private Callback<T> mCallback;
    private Callback<Bundle> mPermissionRouter;
    private static final short REQUEST_PERMISSION = 1010;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCallback != null && data != null) {
            if (data.hasExtra(KEY_DATA)) {
                mCallback.result((T) data.getExtras().get(KEY_DATA));
            } else {
                mCallback.result(null);
            }
        }
    }

    protected void showDialog(Class dialog, Bundle bundle, Callback<T> callback) {
        AbsDialog d = (AbsDialog) AbsDialog.instantiate(this, dialog.getName(), bundle);
        if (callback != null) d.setCallback(callback);
        d.show(getSupportFragmentManager(), dialog.getName());
    }

    public void startActivityAndClearStack(Class activity) {
        Intent intent = new Intent(this, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    protected <K extends String, V> void switchActivity(Class act) {
        switchActivity(act, null, NO_REQUEST, null);
    }

    protected <K extends String, V> void switchActivity(@NonNull Class activity, @Nullable HashMap<K, V> map,
                                                        int request, @Nullable Callback<T> router) {
        mCallback = router;
        Intent intent = new Intent(this, activity);
        if (map != null) {
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pairs = (Map.Entry)iterator.next();
                V value = (V) pairs.getValue();
                if (value instanceof Integer)
                    intent.putExtra((K) pairs.getKey(), (Integer) value);
                else if (value instanceof String)
                    intent.putExtra((K) pairs.getKey(), (String) value);
                else if (value instanceof Boolean)
                    intent.putExtra((K) pairs.getKey(), (Boolean) value);
                else if (value instanceof Parcelable)
                    intent.putExtra((K) pairs.getKey(), (Parcelable) value);
                else if (value instanceof ArrayList)
                    intent.putExtra((K) pairs.getKey(), (ArrayList<Parcelable>) value);
                iterator.remove();
            }
        }

        if (request == NO_REQUEST) {
            startActivity(intent);
        } else if (request == PARAM_CLEAR_STACK) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (request == PARAM_CLEAR_TOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            startActivityForResult(intent, request);
        }
    }

    protected void deliverResult(T data) {
        Intent intent = new Intent();
        if (data instanceof String)
            intent.putExtra(KEY_DATA, (String) data);
        else if (data instanceof Integer)
            intent.putExtra(KEY_DATA, (Integer) data);
        else if (data instanceof Boolean)
            intent.putExtra(KEY_DATA, (Boolean) data);
        else if (data instanceof Parcelable)
            intent.putExtra(KEY_DATA, (Parcelable) data);

        setResult(RESULT_OK, intent);
        finish();
    }

    @NonNull
    public <F extends AbsFragment> F showMenuFragment(@NonNull Class fragment, int id, @Nullable Callback<T> router) {
        return showFragment(fragment, Bundle.EMPTY, false, id, router);
    }

    @NonNull
    public <F extends AbsFragment> F showFragment(@NonNull Class fragment, @NonNull Bundle bundle, @NonNull Boolean addToBackStack, @Nullable Callback<T> router) {
        return showFragment(fragment, bundle, addToBackStack, R.id.fragment, router);
    }

    @NonNull
    public <F extends AbsFragment> F showFragment(@NonNull Class fragment, @NonNull Bundle bundle,
                                                  @NonNull Boolean addToBackStack, int id, @Nullable Callback<T> router) {
        F f = (F) AbsFragment.instantiate(this, fragment.getName(), bundle);
        f.setCallback(router);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) transaction.addToBackStack(fragment.getName());
        transaction.replace(id, f);
        transaction.commitAllowingStateLoss();
        return f;
    }

    @NonNull
    public <F extends AbsFragment> F addFragment(@NonNull Class fragment, @NonNull Bundle bundle,
                                                  @NonNull Boolean addToBackStack, int id, @Nullable Callback<T> router) {
        F f = (F) AbsFragment.instantiate(this, fragment.getName(), bundle);
        f.setCallback(router);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) transaction.addToBackStack(fragment.getName());
        transaction.add(id, f);
        transaction.commitAllowingStateLoss();
        return f;
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mCallback = null;
//    }

    public void requestPermission(Callback<Bundle> router, String... permissions) {
        mPermissionRouter = router;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_PERMISSION);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("permission", permission);
                    bundle.putBoolean("granted", true);
                    mPermissionRouter.result(bundle);
                }
            }
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("permission", Arrays.toString(permissions));
            bundle.putBoolean("granted", true);
            mPermissionRouter.result(bundle);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION && mPermissionRouter != null) {
            Bundle bundle = new Bundle();
            bundle.putString("permission", permissions[0]);
            bundle.putBoolean("granted", grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
            mPermissionRouter.result(bundle);
        }
    }

    public int optColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(this, colorRes);
    }

    @NonNull
    protected String getAction(Bundle bundle) {
        if (bundle == null) {
            return "";
        }

        return bundle.getString("action", "");
    }

    protected void sendLocalAction(@NonNull String action) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(action));
    }
}
