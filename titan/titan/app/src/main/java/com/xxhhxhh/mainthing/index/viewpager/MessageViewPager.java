package com.xxhhxhh.mainthing.index.viewpager;

import android.content.Context;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class MessageViewPager extends ViewPager {

    public MessageViewPager(@NonNull Context context) {
        super(context);
    }

    private boolean scrollable = true;//可滚动标记位
    private float xlast = 0;//上一次位置
    private float ylast = 0;
    private boolean isFatherScrollable = false;//父组件默认不响应事件

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

        return isScrollable() && super.onTouchEvent(event);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {

        final float x = event.getX();
        final float y = event.getY();

        //允许范围
        if(Math.abs(xlast - x) < 10 && Math.abs(ylast - y) > 10)
        {
            isFatherScrollable = false;
        }
        else
        {
            isFatherScrollable = true;
        }

        xlast = event.getX();
        ylast = event.getY();

        return isFatherScrollable &&  super.onInterceptTouchEvent(event);
    }

}
