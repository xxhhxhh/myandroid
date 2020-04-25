package com.xxhhxhh.mainthing.pagechanges;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.xxhhxhh.mainthing.index.fragment.OneLabelForArticleFragment;
public class SwitchArticleLabelAdapter extends FragmentPagerAdapter
{
    private int size;
    public void setSize(int size) { this.size = size; }
    public int getSize() { return size; }

    public SwitchArticleLabelAdapter(@NonNull FragmentManager fm, int behavior)
    {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return new OneLabelForArticleFragment(position);
    }


    @Override
    public int getCount() {
        return size;
    }
}
