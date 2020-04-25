package com.xxhhxhh.mainthing.index.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.titan.R;
import com.xxhhxhh.mainthing.MainActivity;
import com.xxhhxhh.mainthing.index.viewpager.LabelBarViewPager;
import com.xxhhxhh.mainthing.locoldatabase.SQLiteDataBaseOpenHelper;
import com.xxhhxhh.mainthing.pagechanges.ArticleLabelTabSelectedListener;
import com.xxhhxhh.mainthing.pagechanges.SwitchArticleLabelAdapter;
import com.xxhhxhh.mainthing.pagechanges.SwitchSuiJiLabelAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ArticleFragment extends Fragment
{

    public int position;
    private LinearLayout main;
    public static List<String> labels = new ArrayList<>();
    public ArticleFragment(int position)
    {
        this.position = position;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        labels = new ArrayList<>();
        SQLiteDataBaseOpenHelper db = new SQLiteDataBaseOpenHelper(context, MainActivity.databasePath, null, MainActivity.newDataBaseVersion);
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from ArticleLabels;", null);
        cursor.moveToFirst();
        labels.add(cursor.getString(0));
        while (cursor.moveToNext())
        {
            labels.add(cursor.getString(0));
        }
        db.close();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        main = (LinearLayout) inflater.inflate(R.layout.label_index, container, false);
        init();

        return main;
    }

    //初始化
    private void init()
    {
        TabLayout tabLayout = main.findViewById(R.id.indexLabel);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //添加标签
        for(String key : labels)
        {
            TabLayout.Tab tabItem = tabLayout.newTab();
            tabItem.setText(key);
            tabLayout.addTab(tabItem);
        }


        LabelBarViewPager labelBarViewPager = new LabelBarViewPager(getContext());
        labelBarViewPager.setId(R.id.showMain);
        SwitchArticleLabelAdapter switchArticleLabelAdapter = new SwitchArticleLabelAdapter(getChildFragmentManager(), 1);
        switchArticleLabelAdapter.setSize(1);
        labelBarViewPager.setAdapter(switchArticleLabelAdapter);
        ArticleLabelTabSelectedListener articleLabelTabSelectedListener = new ArticleLabelTabSelectedListener(labelBarViewPager);
        tabLayout.addOnTabSelectedListener(articleLabelTabSelectedListener);
        labelBarViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        labelBarViewPager.setScrollAble(true);
        labelBarViewPager.setCurrentItem(0);
        ((RelativeLayout)main.findViewById(R.id.switchOneLabel)).addView(labelBarViewPager);
    }
}
