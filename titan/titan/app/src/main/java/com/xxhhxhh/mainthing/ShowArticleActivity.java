package com.xxhhxhh.mainthing;

import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.titan.R;
import com.xxhhxhh.mainthing.databean.ArticleDataBean;
import com.xxhhxhh.mainthing.pagechanges.ArticleSwtichCommentAdapter;
import com.xxhhxhh.mainthing.viewpager.ShowMessageViewPager;
import com.google.android.material.tabs.TabLayout;

public class ShowArticleActivity extends FragmentActivity
{
    public static ShowMessageViewPager viewPager;
    private ArticleDataBean articleDataBean;
    public ArticleDataBean getArticleDataBean() { return articleDataBean; }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_article);
        articleDataBean = (ArticleDataBean) getIntent().getSerializableExtra("data");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        LinearLayout main = findViewById(R.id.showArticleOrComment);
        viewPager = new ShowMessageViewPager(this);
        main.addView(viewPager);
        TabLayout tabLayout = findViewById(R.id.switchCommentOrContent);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setId(R.id.showMain);
        ArticleSwtichCommentAdapter articleSwtichCommentAdapter = new ArticleSwtichCommentAdapter(getSupportFragmentManager(), 1);
        viewPager.setAdapter(articleSwtichCommentAdapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onBackPressed()
    {
        if(viewPager.getCurrentItem() == 0)
        {
            this.finish();
        }
        else
        {
            viewPager.setCurrentItem(0);
        }
    }
}
