package com.xxhhxhh.mainthing.viewpager;

import android.content.Context;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class MainViewPager extends ViewPager {

    private boolean scrollable = true;

    public MainViewPager(@NonNull Context context) {
        super(context);
    }

    public void setScrollAble(boolean scrollable)
    {
        this.scrollable = scrollable;
    }

    public boolean isScrollable()
    {
        return scrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        scrollTo(getWidth() * getCurrentItem(), 0);
        return isScrollable() && super.onTouchEvent(event);
    }



}
