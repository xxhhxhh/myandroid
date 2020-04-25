package com.xxhhxhh.mainthing.pagechanges;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.xxhhxhh.mainthing.index.fragment.IndexFragment;
import com.xxhhxhh.mainthing.mine.MineFragment;
import com.xxhhxhh.mainthing.search.SearchFragment;

public class BottomPageChangerAdapter extends FragmentPagerAdapter {

    public BottomPageChangerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            //搜索
            case 1:
            {
                return new SearchFragment();
            }
            //我的页
            case 2:
            {
                return new MineFragment();
            }
            //默认首页
            default:
            {
                return new IndexFragment();
            }
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
