package com.xxhhxhh.mainthing.pagechanges;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.xxhhxhh.mainthing.show.commentsandreply.ShowCommentsFragment;
import com.xxhhxhh.mainthing.show.showarticle.ShowArticleFragment;

public class ArticleSwtichCommentAdapter extends FragmentPagerAdapter
{
    public ArticleSwtichCommentAdapter(@NonNull FragmentManager fm, int behavior)
    {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        if (position == 1)
        {
            return new ShowCommentsFragment();
        }
        return new ShowArticleFragment();
    }

    @Override
    public int getCount()
    {
        return 2;
    }
}
