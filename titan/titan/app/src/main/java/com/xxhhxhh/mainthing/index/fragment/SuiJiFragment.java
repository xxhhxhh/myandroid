package com.xxhhxhh.mainthing.index.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.titan.R;
import com.xxhhxhh.mainthing.MainActivity;
import com.xxhhxhh.mainthing.index.viewpager.LabelBarViewPager;
import com.xxhhxhh.mainthing.locoldatabase.SQLiteDataBaseOpenHelper;
import com.xxhhxhh.mainthing.pagechanges.SuiJiLabelTabSelectedListener;
import com.xxhhxhh.mainthing.pagechanges.SwitchSuiJiLabelAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SuiJiFragment extends Fragment
{

    private RecyclerView views;
    private int position;
    private LinearLayout main;
    public static List<String> labels = new ArrayList<>();//标签
    public SuiJiFragment(int position)
    {
        this.position = position;
    }


    @Override
    public void onCreate(Bundle saveInstance)
    {
        super.onCreate(saveInstance);
        SQLiteDataBaseOpenHelper db = new SQLiteDataBaseOpenHelper(getContext(), MainActivity.databasePath, null, MainActivity.newDataBaseVersion);
        Cursor cursor = db.getReadableDatabase().rawQuery("select * from SuiJiLabels;", null);
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        main = (LinearLayout) inflater.inflate(R.layout.label_index, container, false);

        init();

        return main;
    }

    //初始化
    private void init()
    {
        TabLayout tabLayout = main.findViewById(R.id.indexLabel);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        for(String key : labels)
        {
            TabLayout.Tab tabItem = tabLayout.newTab();
            tabItem.setText(key);
            tabLayout.addTab(tabItem);
        }

        LabelBarViewPager labelBarViewPager = new LabelBarViewPager(getContext());
        labelBarViewPager.setId(R.id.showMain);
        SwitchSuiJiLabelAdapter switchSuiJiLabelAdapter = new SwitchSuiJiLabelAdapter(getChildFragmentManager(), 1);
        switchSuiJiLabelAdapter.setSize(1);
        labelBarViewPager.setAdapter(switchSuiJiLabelAdapter);
        SuiJiLabelTabSelectedListener suiJiLabelTabSelectedListener = new SuiJiLabelTabSelectedListener(labelBarViewPager);
        tabLayout.addOnTabSelectedListener(suiJiLabelTabSelectedListener);
        labelBarViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        labelBarViewPager.setScrollAble(true);
        labelBarViewPager.setCurrentItem(0);
        ((RelativeLayout)main.findViewById(R.id.switchOneLabel)).addView(labelBarViewPager);
    }


}
