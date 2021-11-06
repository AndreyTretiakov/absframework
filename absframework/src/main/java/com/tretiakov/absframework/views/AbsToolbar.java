package com.tretiakov.absframework.views;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
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

    public void setTitle(SpannableStringBuilder title) {
        ((android.widget.TextView) findViewById(R.id.toolbarTitle)).setText(title);
    }

    public void setArrow(View.OnClickListener cl) {
        DrawerArrowDrawable arrow = new DrawerArrowDrawable(getContext());
        arrow.setColor(ContextCompat.getColor(getContext(), R.color.abs_main_icon_tint_contrast));
        arrow.setProgress(1);
        setNavigationIcon(arrow);
        setNavigationOnClickListener(cl);
    }

    public void setClose(View.OnClickListener cl) {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_action_close);
        drawable.setTint(optColor(R.color.abs_main_icon_tint));
        setNavigationIcon(drawable);
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

    public int optColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }
}
