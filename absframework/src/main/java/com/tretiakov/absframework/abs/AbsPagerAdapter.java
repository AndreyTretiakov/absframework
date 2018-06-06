package com.tretiakov.absframework.abs;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbsPagerAdapter<F extends Fragment> extends FragmentPagerAdapter {

    private List<F> mItems = new ArrayList<F>();
    private ArrayList<String> mTitles = new ArrayList<>();

    public AbsPagerAdapter(AbsActivity activity) {
        super(activity.getSupportFragmentManager());
    }

    public AbsPagerAdapter(Fragment fragment) {
        super(fragment.getChildFragmentManager());
    }

    public void addObjects(List<F> objects) {
        if (objects != null) {
            mItems = objects;
        }
    }

    public void addObjectsWithTitles(List<F> objects, String[] titles) {
        if (mTitles != null) {
            Collections.addAll(mTitles, titles);
        }

        if (objects != null) {
            mItems = objects;
        }
    }

    public F getItem(int position) {
        return mItems.get(position);
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.isEmpty() ? "" : mTitles.get(position);
    }

    public int getCount() {
        return isEmpty() ? 0 : mItems.size();
    }

    @NonNull
    public List<F> getItems() {
        return mItems;
    }

    public void clear() {
        mItems.clear();
    }

    private boolean isEmpty() {
        return mItems == null || mItems.isEmpty();
    }

    public boolean fragmentsHasData() {
        if (isEmpty()) {
            return false;
        }

        Fragment fragment = getItem(0);
        return fragment != null && fragment.getView() != null;
    }

}
