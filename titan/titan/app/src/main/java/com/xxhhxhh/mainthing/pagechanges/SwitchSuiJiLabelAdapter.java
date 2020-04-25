package com.xxhhxhh.mainthing.pagechanges;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.xxhhxhh.mainthing.index.fragment.OneLabelForArticleFragment;
import com.xxhhxhh.mainthing.index.fragment.OneLabelForSuiJiFragment;

public class SwitchSuiJiLabelAdapter extends FragmentStatePagerAdapter
{

    private int size;
    public void setSize(int size) { this.size = size; }
    public int getSize() { return size; }


    public SwitchSuiJiLabelAdapter(@NonNull FragmentManager fm, int behavior)
    {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        System.out.println("adapter");
        return new OneLabelForSuiJiFragment(position);
    }


    @Override
    public int getCount() {
        return size;
    }
}
