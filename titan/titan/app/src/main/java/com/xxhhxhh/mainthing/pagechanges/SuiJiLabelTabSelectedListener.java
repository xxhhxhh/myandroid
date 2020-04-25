package com.xxhhxhh.mainthing.pagechanges;

import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.xxhhxhh.mainthing.index.fragment.SuiJiFragment;

public class SuiJiLabelTabSelectedListener extends TabLayout.ViewPagerOnTabSelectedListener
{
    private ViewPager viewPager;

    public SuiJiLabelTabSelectedListener(ViewPager viewPager)
    {
        super(viewPager);
        this.viewPager = viewPager;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab)
    {

        SwitchSuiJiLabelAdapter adapter = (SwitchSuiJiLabelAdapter) viewPager.getAdapter();
        int a = adapter.getSize();
        //长度不足，补充
        if(a <= tab.getPosition() + 1)
        {
            adapter.setSize(a + 1);
            adapter.getItem(a + 1);
            adapter.notifyDataSetChanged();
            viewPager.setCurrentItem(tab.getPosition() + 1);
            tab.parent.getTabAt(tab.parent.getSelectedTabPosition()).select();
        }
        //直接跳转
       else
        {
            super.onTabSelected(tab);
        }

    }
}
