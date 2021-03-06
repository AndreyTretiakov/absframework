package com.tretiakov.absframework.abs;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.tretiakov.absframework.R;
import com.tretiakov.absframework.constants.AbsConstants;
import com.tretiakov.absframework.routers.IRouter;
import com.tretiakov.absframework.routers.TypedFilter;
import com.tretiakov.absframework.utils.Keyboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
@SuppressWarnings("unchecked")
public abstract class AbsRAdapter <E, H extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<H> implements AbsConstants {

    private IRouter mRouter;
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

    public AbsRAdapter(Context context, List<E> items, IRouter router) {
        mInflater = LayoutInflater.from(context);
        mRouter = router;
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

    public void setHasFooter(@Nullable E footer, boolean value) {
        mHasFooter = value;
        if (value) mFooter = footer;
    }

    public void setItems(List<E> items, boolean needRefresh) {
        mItems = items;
        notifyItems(needRefresh);
    }

    public void setItems(E[] items, boolean needRefresh) {
        mItems.clear();
        Collections.addAll(mItems, items);
        notifyItems(needRefresh);
    }

    public void addItem(@NonNull E item, boolean needRefresh) {
        mItems.add(item);
        if (needRefresh)
            notifyItemInserted(mItems.size());
    }

    public void addItems(E[] items, boolean needRefresh) {
        Collections.addAll(mItems, items);
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

        if (needRefresh)
            notifyItemRemoved(index);

        return result;
    }

    public void remove(int position, boolean needRefresh) {
        if (mItems != null && !mItems.isEmpty() && position < mItems.size())
            mItems.remove(position);

        if (needRefresh)
            notifyItemRemoved(position);
    }

    public void clear(boolean needRefresh) {
        if (mItems != null && !mItems.isEmpty()) {
            mItems.clear();
            notifyItems(needRefresh);
        }
    }

    protected void notifyItems(boolean needRefresh) {
        if (needRefresh)
            notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItems.size() + (mHasFooter ? 1 : 0);
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
            mRouter.onData(data);
            return true;
        }

        return false;
    }

    @Nullable
    protected H getHolder(int position) {
        try {
            return (H) mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(position));
        } catch (NullPointerException e) {
            Log.e(AbsRAdapter.class.getSimpleName(), e.getMessage());
            return null;
        }
    }

    @Override
    public void onBindViewHolder(H h, int position) {
        h.itemView.setTag(R.string.tag_position, position);
        onView(h, getItem(position), position);
    }

    protected abstract void onView(H h, E item, int pos);

    protected View.OnClickListener onClick = v ->
            onData(getItem((Integer) v.getTag(R.string.tag_position)));
    
    /** RESOURCES METHODS */

    protected Context getContext() {
        return mContext;
    }

    protected String getString(@StringRes int res) {
        return mContext.getString(res);
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

}
