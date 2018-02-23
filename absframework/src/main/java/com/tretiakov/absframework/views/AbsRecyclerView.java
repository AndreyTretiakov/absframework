package com.tretiakov.absframework.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.tretiakov.absframework.R;
import com.tretiakov.absframework.utils.Utils;

/**
 * @author Andrey Tretiakov. Created 4/16/2016.
 */
public class AbsRecyclerView extends RecyclerView {

    private int maxHeight = -1;

    public AbsRecyclerView(Context context) {
        super(context);
    }

    public AbsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            int verticalSpaceHeight;
            boolean hasItemAnimator;
            boolean hasDivider;

            String type;
            int columnCount;
            int spaceSize;

            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsRecyclerView);
            verticalSpaceHeight = (int) a.getDimension(R.styleable.AbsRecyclerView_divider_height, 0);
            hasDivider = a.getBoolean(R.styleable.AbsRecyclerView_has_divider, false);
            hasItemAnimator = a.getBoolean(R.styleable.AbsRecyclerView_has_item_animator, false);

            type = a.getString(R.styleable.AbsRecyclerView_type);
            columnCount = a.getInt(R.styleable.AbsRecyclerView_columnCount, 0);
            spaceSize = a.getDimensionPixelOffset(R.styleable.AbsRecyclerView_spaceSize, 0);

            if (!TextUtils.isEmpty(type)) {
                switch (type) {
                    case "linear":
                        setLayoutManager(new LinearLayoutManager(getContext()));
                        break;
                    case "grid":
                        setLayoutManager(new GridLayoutManager(getContext(), columnCount));
                        addItemDecoration(new ItemDecorationAlbumColumns(spaceSize, columnCount));
                        break;
                }
            }
            a.recycle();

            if (hasItemAnimator) setItemAnimator(new DefaultItemAnimator());
            if (hasDivider && verticalSpaceHeight ==0) addItemDecoration(new VerticalSpaceItemDecoration((int) getResources().getDimension(R.dimen.default_divider_height)));
            else if (verticalSpaceHeight != 0) addItemDecoration(new VerticalSpaceItemDecoration(verticalSpaceHeight));
        }
    }

    private static class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = mVerticalSpaceHeight;
        }

    }

    private class ItemDecorationAlbumColumns extends RecyclerView.ItemDecoration {

        private int mSizeGridSpacingPx;
        private int mGridSize;

        private boolean mNeedLeftSpacing = false;

        public ItemDecorationAlbumColumns(int gridSpacingPx, int gridSize) {
            mSizeGridSpacingPx = gridSpacingPx;
            mGridSize = gridSize;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int frameWidth = (int) ((parent.getWidth() - (float) mSizeGridSpacingPx * (mGridSize - 1)) / mGridSize);
            int padding = parent.getWidth() / mGridSize - frameWidth;
            int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
            if (itemPosition < mGridSize) {
                outRect.top = 0;
            } else {
                outRect.top = mSizeGridSpacingPx;
            }
            if (itemPosition % mGridSize == 0) {
                outRect.left = 0;
                outRect.right = padding;
                mNeedLeftSpacing = true;
            } else if ((itemPosition + 1) % mGridSize == 0) {
                mNeedLeftSpacing = false;
                outRect.right = 0;
                outRect.left = padding;
            } else if (mNeedLeftSpacing) {
                mNeedLeftSpacing = false;
                outRect.left = mSizeGridSpacingPx - padding;
                if ((itemPosition + 2) % mGridSize == 0) {
                    outRect.right = mSizeGridSpacingPx - padding;
                } else {
                    outRect.right = mSizeGridSpacingPx / 2;
                }
            } else if ((itemPosition + 2) % mGridSize == 0) {
                mNeedLeftSpacing = false;
                outRect.left = mSizeGridSpacingPx / 2;
                outRect.right = mSizeGridSpacingPx - padding;
            } else {
                mNeedLeftSpacing = false;
                outRect.left = mSizeGridSpacingPx / 2;
                outRect.right = mSizeGridSpacingPx / 2;
            }
            outRect.bottom = 0;
        }
    }

    public void setMaxHeight(int height) {
        maxHeight = height;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (maxHeight != -1) {
            heightSpec = MeasureSpec.makeMeasureSpec(dpToPx(maxHeight), MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthSpec, heightSpec);
    }

    public int dpToPx(int dp) {
        Resources r = getContext().getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
}
