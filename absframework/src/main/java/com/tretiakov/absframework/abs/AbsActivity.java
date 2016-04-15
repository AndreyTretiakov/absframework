package com.tretiakov.absframework.abs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tretiakov.absframework.R;
import com.tretiakov.absframework.constants.Constants;
import com.tretiakov.absframework.routers.IRouter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
@SuppressWarnings("unchecked")
public class AbsActivity<T> extends AppCompatActivity implements Constants {

    private IRouter<T> mRouter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mRouter != null) {
            if (data.hasExtra(KEY_DATA)) {
                mRouter.onData((T) data.getExtras().get(KEY_DATA));
            } else {
                mRouter.onData(null);
            }
        }
    }

    public void startActivityAndClearStack(Class activity) {
        Intent intent = new Intent(this, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    protected <K extends String, V> void switchActivity(@NonNull Class activity, @Nullable HashMap<K, V> map,
                                                        int request, @Nullable IRouter<T> router) {
        mRouter = router;
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
    public <F extends AbsFragment> F showFragment(@NonNull Class fragment, @NonNull Bundle bundle, @NonNull Boolean addToBackStack, @Nullable IRouter<T> router) {
        return showFragment(fragment, bundle, addToBackStack, R.id.fragment, router);
    }

    @NonNull
    public <F extends AbsFragment> F showFragment(@NonNull Class fragment, @NonNull Bundle bundle,
                                                  @NonNull Boolean addToBackStack, int id, @Nullable IRouter<T> router) {
        F f = (F) AbsFragment.instantiate(this, fragment.getName(), bundle);
        if (router != null) f.setCallback(router);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) transaction.addToBackStack(fragment.getName());

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(id, f);
        transaction.commit();
        return f;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRouter = null;
    }
}
