package com.tretiakov.absframework.abs;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;

import com.tretiakov.absframework.R;
import com.tretiakov.absframework.constants.AbsConstants;
import com.tretiakov.absframework.routers.AbsCallback;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
@SuppressWarnings("unchecked")
public abstract class AbsActivity extends AppCompatActivity implements AbsConstants {

    private AbsCallback mAbsCallback;
    private AbsCallback<Bundle> mPermissionRouter;
    private static final short REQUEST_PERMISSION = 1010;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        new Handler().post(() -> {
            if (mAbsCallback != null && data != null) {
                if (data.hasExtra(KEY_DATA)) {
                    mAbsCallback.result(data.getExtras().get(KEY_DATA));
                } else {
                    mAbsCallback.result(null);
                }
            }
        });
    }

    protected void showAlertDialog(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(null);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }

    protected void showAlertDialog(String msg, DialogInterface.OnClickListener listener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(null);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok), listener);
        alertDialog.show();
    }

    protected void showAlertDialog2options(String msg, DialogInterface.OnClickListener listener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(null);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), listener);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok), listener);
        alertDialog.show();
    }

    protected void showUnCancelableDialog(Class dialog, Bundle bundle, AbsCallback absCallback) {
        AbsDialog d = (AbsDialog) AbsDialog.instantiate(this, dialog.getName(), bundle);
        if (absCallback != null) d.setCallback(absCallback);
        d.setCancelable(false);
        d.show(getSupportFragmentManager(), dialog.getName());
    }

    protected void showDialog(Class dialog, Bundle bundle, AbsCallback absCallback) {
        AbsDialog d = (AbsDialog) AbsDialog.instantiate(this, dialog.getName(), bundle);
        if (absCallback != null) d.setCallback(absCallback);
        d.show(getSupportFragmentManager(), dialog.getName());
    }

    protected void showBottomSheet(Class dialog, Bundle bundle, AbsCallback absCallback) {
        AbsBottomSheetDialog d = (AbsBottomSheetDialog) AbsDialog.instantiate(this, dialog.getName(), bundle);
        if (absCallback != null) d.setCallback(absCallback);
        d.show(getSupportFragmentManager(), dialog.getName());
    }

    public void startActivityAndClearStack(Class activity) {
        Intent intent = new Intent(this, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    protected void switchActivity(Class act) {
        switchActivity(act, null, NO_REQUEST, null);
    }

    protected void switchActivity(Class act, Bundle bundle) {
        switchActivity(act, bundle, NO_REQUEST, null);
    }

    protected void switchActivity(Class act, int request, Bundle bundle) {
        switchActivity(act, bundle, request, null);
    }

    protected void switchActivity(Class act, @Nullable Bundle bundle, AbsCallback router) {
        switchActivity(act, bundle, NO_REQUEST, router);
    }

    protected void switchActivity(@NonNull Class activity, @Nullable Bundle bundle, int request,
                                  @Nullable AbsCallback router) {
        mAbsCallback = router;
        Intent intent = new Intent(this, activity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        if (request == NO_REQUEST) {
            startActivity(intent);
        } else if (request == PARAM_FINISH) {
            startActivity(intent);
            finish();
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

    protected <T> void deliverResult(T data) {
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
    public <F extends AbsFragment> F showMenuFragment(@NonNull Class fragment, int id, @Nullable AbsCallback router) {
        return showFragment(fragment, Bundle.EMPTY, false, id, router);
    }

    @NonNull
    public <F extends AbsFragment> F showFragment(@NonNull Class fragment, @Nullable AbsCallback router) {
        return showFragment(fragment, Bundle.EMPTY, true, R.id.fragment, router);
    }

    @NonNull
    public <F extends AbsFragment> F showFragment(@NonNull Class fragment, @NonNull Bundle bundle, @NonNull Boolean addToBackStack, @Nullable AbsCallback router) {
        return showFragment(fragment, bundle, addToBackStack, R.id.fragment, router);
    }

    public Fragment showKFragment(@NonNull Fragment fragment) {
        return showKFragment(fragment, Bundle.EMPTY, true, R.id.fragment, null);
    }

    public Fragment showKFragment(@NonNull Fragment fragment, @NonNull Bundle bundle, @Nullable AbsCallback router) {
        return showKFragment(fragment, bundle, true, R.id.fragment, router);
    }

    @NonNull
    public Fragment showKFragment(@NonNull Fragment fragment, @NonNull Bundle bundle, @NonNull Boolean addToBackStack, @Nullable AbsCallback router) {
        return showKFragment(fragment, bundle, addToBackStack, R.id.fragment, router);
    }

    @NonNull
    public <F extends AbsFragment> F showFragment(@NonNull Class fragment, @NonNull Bundle bundle,
                                                  @NonNull Boolean addToBackStack, int id, @Nullable AbsCallback router) {
        F f = (F) AbsFragment.instantiate(this, fragment.getName(), bundle);
        f.setCallback(router);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) transaction.addToBackStack(fragment.getName());
        transaction.replace(id, f);
        transaction.commitAllowingStateLoss();
        return f;
    }

    @NonNull
    public Fragment showKFragment(@NonNull Fragment fragment, @NonNull Bundle bundle,
                                                  @NonNull Boolean addToBackStack, int id, @Nullable AbsCallback router) {
        fragment.setArguments(bundle);
        ((KAbsFragment)fragment).setCallback(router);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) transaction.addToBackStack(fragment.getClass().getName());
        transaction.replace(id, fragment);
        transaction.commitAllowingStateLoss();
        return fragment;
    }

    @NonNull
    public <F extends AbsFragment> F addFragment(@NonNull Class fragment, @NonNull Bundle bundle,
                                                  @NonNull Boolean addToBackStack, int id, @Nullable AbsCallback router) {
        F f = (F) AbsFragment.instantiate(this, fragment.getName(), bundle);
        f.setCallback(router);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) transaction.addToBackStack(fragment.getName());
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(id, f);
        transaction.commitAllowingStateLoss();
        return f;
    }

    @NonNull
    public Fragment addKotlinFragment(@NonNull Fragment fragment, @NonNull Bundle bundle,
                                      @NonNull Boolean addToBackStack, int id, @Nullable AbsCallback router) {
        fragment.setArguments(bundle);
        ((KAbsFragment)fragment).setCallback(router);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) transaction.addToBackStack(fragment.getClass().getName());
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(id, fragment);
        transaction.commitAllowingStateLoss();
        return fragment;
    }

    @NonNull
    public <F extends KAbsFragment> F addKotlinFragmentRTL(@NonNull F fragment, @NonNull Bundle bundle,
                                                     @NonNull Boolean addToBackStack, int id, @Nullable AbsCallback router) {
        fragment.setArguments(bundle);
        fragment.setCallback(router);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) transaction.addToBackStack(fragment.getClass().getName());
//        transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right,
//                R.animator.slide_in_left, R.animator.slide_in_right);
        transaction.add(id, fragment);
        transaction.commitAllowingStateLoss();
        return fragment;
    }

    @NonNull
    public <F extends AbsFragment> F addFragmentRTL(@NonNull Class fragment, @NonNull Bundle bundle,
                                                 @NonNull Boolean addToBackStack, int id, @Nullable AbsCallback router) {
        F f = (F) AbsFragment.instantiate(this, fragment.getName(), bundle);
        f.setCallback(router);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) transaction.addToBackStack(fragment.getName());
//        transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right,
//                R.animator.slide_in_left, R.animator.slide_in_right);
        transaction.add(id, f);
        transaction.commitAllowingStateLoss();
        return f;
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mCallback = null;
//    }

    protected <T> void onData(@Nullable T data, boolean needBack) {
        if (mAbsCallback != null) {
            mAbsCallback.result(data);
        }

        if (needBack) {
            onBackPressed();
        }
    }

    public void requestPermission(@NonNull AbsCallback<Bundle> router, String... permissions) {
        mPermissionRouter = router;
        if (permissions == null || permissions.length == 0) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("granted", false);
            mPermissionRouter.result(bundle);
            return;
        }

        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION);
        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean("granted", true);
            mPermissionRouter.result(bundle);
        }
    }

    public void requestPermissions(@NonNull AbsCallback<Bundle> router, String... permissions) {
        mPermissionRouter = router;
        if (permissions == null || permissions.length == 0) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("granted", false);
            mPermissionRouter.result(bundle);
            return;
        }

        ArrayList<String> toRequest = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                toRequest.add(permission);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("permission", permission);
                bundle.putBoolean("granted", true);
                mPermissionRouter.result(bundle);
            }
        }

        if (!toRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    toRequest.toArray(new String[0]),
                    REQUEST_PERMISSION);
        }
    }

    protected boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    protected boolean allPermissionsGranted(String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION && mPermissionRouter != null) {
            for (int i = 0; i < permissions.length; i++) {
                final String permission = permissions[i];
                Bundle bundle = new Bundle();
                bundle.putString("permission", permission);
                bundle.putBoolean("granted", grantResults[i] == PackageManager.PERMISSION_GRANTED);
                mPermissionRouter.result(bundle);
            }

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

    private FragmentFactory getFragmentFactory() {
        return getSupportFragmentManager().getFragmentFactory();
    }

    public void findViewsByIds(View.OnClickListener listener, @IdRes int... ids) {
        final AppCompatDelegate delegate = getDelegate();
        for (int id : ids) {
            final View view = delegate.findViewById(id);
            if (view != null) {
                view.setOnClickListener(listener);
            }
        }
    }

    public void execute(Runnable runnable) {
        new Thread(runnable).start();
    }
}
