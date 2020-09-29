package com.tretiakov.absframework.views;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.tretiakov.absframework.R;

/**
 * @author Andrew Tretiakov on 3/14/2016.
 */
public class AbsViewPager extends ViewPager {
    public interface OnSimplePageListener {
        void onPageSelect(int position);
    }

    private boolean mIsTouchable;

    public AbsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsViewPager);
        mIsTouchable = a.getBoolean(R.styleable.AbsViewPager_touchable, false);
        a.recycle();
    }

    public boolean onNext() {
        setCurrentItem(getCurrentItem() + 1);
        return true;
    }

    public boolean onPrevious() {
        if (getCurrentItem() == 0)
            return false;

        setCurrentItem(getCurrentItem() - 1);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mIsTouchable && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mIsTouchable && super.onTouchEvent(event);
    }

    public void setOnSimplePageListener(@NonNull OnSimplePageListener listener) {
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                listener.onPageSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
