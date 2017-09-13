package com.tretiakov.absframework.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.tretiakov.absframework.R;

/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
public class AbsToolbar extends Toolbar {

    private int mMenuLayout;

    public AbsToolbar(Context context) {
        super(context);
    }

    public AbsToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsToolbar);
        mMenuLayout = a.getResourceId(R.styleable.AbsToolbar_menu_layout_res, 0);
        a.recycle();

        if (mMenuLayout != 0) inflateMenu(mMenuLayout);
    }

    public void setTitle(@StringRes int title) {
        ((android.widget.TextView) findViewById(R.id.toolbarTitle)).setText(title);
    }

    public void setTitle(String title) {
        ((android.widget.TextView) findViewById(R.id.toolbarTitle)).setText(title);
    }

    public void setArrow(View.OnClickListener cl) {
        DrawerArrowDrawable arrow = new DrawerArrowDrawable(getContext());
        arrow.setColor(ContextCompat.getColor(getContext(), R.color.colorIconDark));
        arrow.setProgress(1);
        setNavigationIcon(arrow);
        setNavigationOnClickListener(cl);
    }

    public void hideBackArrow() {
        setNavigationIcon(null);
        setNavigationOnClickListener(null);
    }

    public void setSearchMode() {
        findViewById(R.id.toolbarTitle).setVisibility(GONE);
        findViewById(R.id.toolbarSearch).setVisibility(VISIBLE);
        findViewById(R.id.toolbarButton).setVisibility(VISIBLE);
    }
}
