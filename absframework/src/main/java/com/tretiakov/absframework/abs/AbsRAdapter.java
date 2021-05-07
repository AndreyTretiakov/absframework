package com.tretiakov.absframework.abs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.tretiakov.absframework.R;
import com.tretiakov.absframework.constants.AbsConstants;
import com.tretiakov.absframework.routers.AbsCallback;
import com.tretiakov.absframework.routers.Callback2;
import com.tretiakov.absframework.routers.TypedFilter;
import com.tretiakov.absframework.utils.Keyboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static androidx.core.os.LocaleListCompat.create;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
@SuppressWarnings("unchecked")
public abstract class AbsRAdapter <E, H extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<H> implements AbsConstants {

    private AbsCallback mRouter;
    private Callback2 mRouter2;
    private Context mContext;
    private List<E> mItems;
    private LayoutInflater mInflater;
    private RecyclerView mRecyclerView;

    private String mPrefix = "";
    private List<E> mOriginalValues;
    private ArrayFilter mFilter;
    private int mSearchMode;

    private E mFooter;
    private boolean mHasFooter;
    private Handler mHandler = new Handler();

    public AbsRAdapter(Context context, List<E> items) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mItems = items;
    }

    public AbsRAdapter(Context context, List<E> items, AbsCallback router) {
        mInflater = LayoutInflater.from(context);
        mRouter = router;
        mContext = context;
        mItems = items;
    }

    public AbsRAdapter(Context context, List<E> items, Callback2 router) {
        mInflater = LayoutInflater.from(context);
        mRouter2 = router;
        mContext = context;
        mItems = items;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Keyboard.hide(getContext(), ((Activity) getContext())
                            .findViewById(android.R.id.content));
                }
            }

        });
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRouter = null;
    }

    protected void setRouter(AbsCallback router) {
        mRouter = router;
    }

    protected void showDialog(Class dialog, Bundle bundle, AbsCallback absCallback) {
        if (mContext instanceof AbsActivity) {
            AbsDialog d = (AbsDialog) AbsDialog.instantiate(getContext(), dialog.getName(), bundle);
            if (absCallback != null) d.setCallback(absCallback);
            d.show(((AbsActivity) mContext).getSupportFragmentManager(), dialog.getName());
        }
    }

    protected void showAlertDialog(String msg, String title) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok),
                (dialog, which) -> alertDialog.dismiss()
        );
        alertDialog.show();
    }

    public void setHasFooter(@Nullable E footer, boolean value) {
        mHasFooter = value;
        if (value) mFooter = footer;
    }

    public void setItems(List<E> items, boolean needRefresh) {
        mItems = items;
        notifyItems(needRefresh);
    }

    public void setItem(int location, E item, boolean needRefresh) {
        mItems.set(location, item);
        notifyItems(needRefresh);
    }

    public void setItems(E[] items, boolean needRefresh) {
        mItems.clear();
        Collections.addAll(mItems, items);
        notifyItems(needRefresh);
    }

    public void addItem(@NonNull E item, boolean needRefresh) {
        mItems.add(item);
        if (needRefresh) notifyItemInserted(mItems.size());
    }

    public void addItem(int position, E item, boolean needRefresh) {
        mItems.add(position, item);
        if (needRefresh) {
            notifyItemInserted(position);
            mHandler.postDelayed(this::notifyDataSetChanged, 500);

            if (position == 0) {
                mRecyclerView.scrollToPosition(0);
            }
        }
    }

    public void addItems(E[] items, boolean needRefresh) {
        Collections.addAll(mItems, items);
        notifyItems(needRefresh);
    }

    public void addItemsToBeginning(List<E> items, boolean needRefresh) {
        Collections.reverse(items);
        for (E element : items) {
            mItems.add(0, element);
        }

        notifyItems(needRefresh);
    }

    public void addItems(Collection<E> items, boolean needRefresh) {
        mItems.addAll(items);
        notifyItems(needRefresh);
    }

    public boolean removeItem(@Nullable E item, boolean needRefresh) {
        if (item == null)
            return false;

        int index = mItems.indexOf(item);
        boolean result = mItems.remove(item);

        if (needRefresh) {
            notifyItemRemoved(index);
            mHandler.postDelayed(this::notifyDataSetChanged, 600);
        }

        return result;
    }

    public void remove(int position, boolean needRefresh) {
        if (mItems != null && !mItems.isEmpty() && position < mItems.size())
            mItems.remove(position);

        if (needRefresh) {
            notifyItemRemoved(position);
            mHandler.postDelayed(this::notifyDataSetChanged, 600);
        }
    }

    public void clear(boolean needRefresh) {
        if (mItems != null && !mItems.isEmpty()) {
            mItems.clear();
            notifyItems(needRefresh);
        }
    }
    public void notifyDataWithDelay() {
        mHandler.postDelayed(this::notifyDataSetChanged, 650);
    }

    protected void notifyItems(boolean needRefresh) {
        if (needRefresh)
            notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItems.size() + (mHasFooter ? 1 : 0);
    }

    public int lastIndex() {
        return getItemCount() - 1;
    }

    public E getItem(int position) {
        return position == getItemCount() ? mFooter : mItems.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mHasFooter ? position == mItems.size() ? FOOTER : ITEM : super.getItemViewType(position);
    }

    @NonNull
    public List<E> getItems() {
        return mItems;
    }

    public boolean isEmpty() {
        return mItems.isEmpty();
    }    

    protected View inflate(@LayoutRes int layout, ViewGroup parent) {
        return mInflater.inflate(layout, parent, false);
    }

    protected <T> boolean onData(@Nullable T data) {
        if (mRouter != null) {
            mRouter.result(data);
            return true;
        }

        return false;
    }

    protected <T> boolean onData2(@Nullable T data, T data2) {
        if (mRouter2 != null) {
            mRouter2.result(data, data2);
            return true;
        }

        return false;
    }

    @NonNull
    protected String getAction(Bundle bundle) {
        if (bundle == null) {
            return "";
        }

        return bundle.getString("action", "");
    }

    @Nullable
    protected H getHolder(int position) {
        try {
            return (H) mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(position));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected <T> void showFragment(Class fragment, Bundle bundle, Boolean addToBackStack, AbsCallback<T> router) {
        if (getContext() instanceof AbsActivity) {
            ((AbsActivity) getContext()).showFragment(fragment, bundle, addToBackStack, router);
        }
    }

    public void showKFragment(@NonNull Fragment fragment) {
        if (getContext() instanceof AbsActivity) {
            ((AbsActivity) getContext()).showKFragment(fragment, Bundle.EMPTY, true, R.id.fragment, null);
        }
    }

    public void showKFragment(@NonNull Fragment fragment, Bundle bundle, @Nullable AbsCallback router) {
        if (getContext() instanceof AbsActivity) {
            ((AbsActivity) getContext()).showKFragment(fragment, bundle, true, R.id.fragment, router);
        }
    }

    protected <T> void addFragment(Class fragment, Bundle bundle, Boolean addToBackStack, AbsCallback<T> router) {
        if (getContext() instanceof AbsActivity) {
            ((AbsActivity) getContext()).addFragment(fragment, bundle, addToBackStack, R.id.fragment, router);
        }
    }

    @Override
    public void onBindViewHolder(H h, int position) {
        h.itemView.setTag(R.string.tag_position, position);
        onView(h, getItem(position), position);
    }

    protected void notifyByPos(int pos) {
        notifyItemChanged(pos);
        mRecyclerView.postDelayed(() -> notifyDataSetChanged(), 500);
    }

    public void notifyAdded() {
        notifyItemInserted(getItemCount() - 1);
        mRecyclerView.postDelayed(() -> notifyDataSetChanged(), 500);
    }

    public void notifyChanged(int position) {
        notifyItemChanged(position);
        mRecyclerView.postDelayed(() -> notifyDataSetChanged(), 200);
    }

    protected void notifyByPos(int pos, int delay) {
        notifyItemChanged(pos);
//        mRecyclerView.postDelayed(() -> notifyDataSetChanged(), delay);
    }

    protected abstract void onView(H h, E item, int pos);

    protected View.OnClickListener onClick = v -> {
        int position = (int) v.getTag(R.string.tag_position);
        E item = getItem(position);
        onData(item);
    };

    /** RESOURCES METHODS */

    protected Context getContext() {
        return mContext;
    }

    protected String getString(@StringRes int res) {
        return mContext.getString(res);
    }

    protected String getString(@StringRes int res, Object... formatArgs) {
        return mContext.getString(res, formatArgs);
    }

    protected int getColor(@ColorRes int color) {
        return ContextCompat.getColor(mContext, color);
    }

    protected int getDimen(@DimenRes int dimen) {
        return (int) mContext.getResources().getDimension(dimen);
    }


    /** SEARCHING FILTER */
    
    public void setPrefix(@Nullable String prefix) {
        mPrefix = prefix;
    }
    
    @NonNull
    protected String getPrefix() {
        return mPrefix == null ? "" : mPrefix;
    }

    public void setSearchModel(@SearchMode int mask) {
        switch (mask) {
            case MODE_SEARCH_ALL:
                break;
            case MODE_SEARCH_START:
                break;
            default: 
                throw new IllegalStateException("Filter mode must be SEARCH_ALL or SEARCH_START");
        }

        mSearchMode = mask;
    }

    @NonNull
    public final Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        
        return mFilter;
    }

    private class ArrayFilter extends Filter {

        ArrayFilter() {
            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<>(mItems);
            }
        }

        @NonNull
        protected FilterResults performFiltering(@Nullable CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (prefix == null || prefix.length() == 0) {
                results.values = mOriginalValues;
                results.count = mOriginalValues.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<E> values = new ArrayList<>(mOriginalValues);
                final ArrayList<E> newValues = new ArrayList<>();

                for (final E value : values) {
                    final String valueText;
                    if (value instanceof TypedFilter) {
                        valueText = ((TypedFilter) value).getSearchable().toLowerCase();
                    } else {
                        valueText = value.toString().toLowerCase();
                    }

                    switch (mSearchMode) {
                        case MODE_SEARCH_START: {
                            if (valueText.startsWith(prefixString)) {
                                newValues.add(value);
                            } else {
                                final String[] words = valueText.split(" ");
                                for (String word : words) {
                                    if (word.startsWith(prefixString)) {
                                        newValues.add(value);
                                        break;
                                    }
                                }
                            }

                            break;
                        }
                        case MODE_SEARCH_ALL: {
                            final String[] searchPrefixes = prefixString.split(" ");

                            boolean addElem = true;
                            for (String pfx : searchPrefixes) {
                                if (!valueText.contains(pfx)) {
                                    addElem = false;
                                    break;
                                }
                            }
                            if (addElem) {
                                newValues.add(value);
                            }

                            break;
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, @NonNull FilterResults results) {
            mItems = (List<E>) results.values;
            notifyDataSetChanged();
        }
    }

    public int optColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    protected TypedArray getTypedArray(int name) {
        return getContext().getResources().obtainTypedArray(name);
    }

    protected String[] getStringArray(int name) {
        return getContext().getResources().getStringArray(name);
    }

}
