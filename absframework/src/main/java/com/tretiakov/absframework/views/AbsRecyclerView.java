package com.tretiakov.absframework.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.tretiakov.absframework.R;

/**
 * @author Andrey Tretiakov. Created 4/16/2016.
 */
public class AbsRecyclerView extends RecyclerView {

    public AbsRecyclerView(Context context) {
        super(context);
    }

    public AbsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        int verticalSpaceHeight;
        boolean hasItemAnimator;
        boolean hasDivider;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsRecyclerView);
        verticalSpaceHeight = (int) a.getDimension(R.styleable.AbsRecyclerView_divider_height, 0);
        hasDivider = a.getBoolean(R.styleable.AbsRecyclerView_has_divider, false);
        hasItemAnimator = a.getBoolean(R.styleable.AbsRecyclerView_has_item_animator, false);
        a.recycle();

        if (hasItemAnimator) setItemAnimator(new DefaultItemAnimator());
        if (hasDivider && verticalSpaceHeight ==0) addItemDecoration(new VerticalSpaceItemDecoration((int) getResources().getDimension(R.dimen.default_divider_height)));
        else if (verticalSpaceHeight != 0) addItemDecoration(new VerticalSpaceItemDecoration(verticalSpaceHeight));
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
}
