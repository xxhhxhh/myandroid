package com.xxhhxhh.mainthing.pagechanges;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.xxhhxhh.mainthing.index.fragment.ArticleFragment;
import com.xxhhxhh.mainthing.index.fragment.SuiJiFragment;

//嵌套继承
public class SwitchSuiJiOrArticle extends FragmentPagerAdapter {

    private int size = 1;
    public int getSize() { return size; }
    public void setSize(int size) {this.size = size; }

    public SwitchSuiJiOrArticle(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {

        switch (position)
        {
            //文章
            case 1:
            {
                return new ArticleFragment(0);
            }//随记
            default:
            {
                return new SuiJiFragment(0);
            }
        }
    }


    @Override
    public int getCount()
    {
        return size;
    }
}
