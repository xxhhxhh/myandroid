package com.xxhhxhh.mainthing.pagechanges;

import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class SwitchSuiJiOrArticleTabSelectListener extends TabLayout.ViewPagerOnTabSelectedListener
{
    private ViewPager viewPager;

    public SwitchSuiJiOrArticleTabSelectListener(ViewPager viewPager)
    {
        super(viewPager);
        this.viewPager = viewPager;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab)
    {
//        super.onTabSelected(tab);
        SwitchSuiJiOrArticle switchSuiJiOrArticle = (SwitchSuiJiOrArticle) viewPager.getAdapter();
        switchSuiJiOrArticle.getItem(tab.getPosition());
        if(switchSuiJiOrArticle.getCount() == 1)
        {
            switchSuiJiOrArticle.setSize(2);
            switchSuiJiOrArticle.notifyDataSetChanged();
        }
        viewPager.setCurrentItem(tab.getPosition());
        tab.parent.getTabAt(tab.getPosition()).select();
    }
}
