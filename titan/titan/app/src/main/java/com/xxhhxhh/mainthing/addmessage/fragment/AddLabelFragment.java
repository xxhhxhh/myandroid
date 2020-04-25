package com.xxhhxhh.mainthing.addmessage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.xxhhxhh.mainthing.AddArticleActivity;
import com.xxhhxhh.mainthing.AddSuiJiActivity;
import com.example.titan.R;
import com.xxhhxhh.mainthing.addmessage.adapter.ChoseLabelsAdapter;
import com.xxhhxhh.mainthing.index.fragment.ArticleFragment;
import com.xxhhxhh.mainthing.index.fragment.SuiJiFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddLabelFragment extends Fragment
    implements View.OnClickListener
{
    public int type;//标识
    public int getType() { return type; }
    public void setType(int type) { this.type = type; }
    //选中标签
    public Map<String, String> chosedLabels;
    public Map<String, String> getChosedLabels()
    {
        if(chosedLabels != null && chosedLabels.isEmpty())
        {
            chosedLabels = new HashMap<>();
        }
        return chosedLabels;
    }

    //管理
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private AddArticleFragment addArticleFragment;
    private AddSuiJiFragment addSuiJiFragment;
    private LinearLayout rootView;//根视图
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        //初始化模式
        if(getType() == 0)
        {
            addSuiJiFragment = (AddSuiJiFragment) manager.findFragmentByTag("addSuiJi");
            ((AddSuiJiActivity)getActivity()).setNowItem(3);
        }
        else if(getType() == 1)
        {
            addArticleFragment = (AddArticleFragment) manager.findFragmentByTag("addArticle");
            ((AddArticleActivity)getActivity()).setNowItem(3);
        }
        rootView = (LinearLayout) inflater.inflate(R.layout.choseing_page, null);
        init();
        return rootView;
    }

    //获取标签数据
    private Map<Integer, String> getData()
    {
        Map<Integer, String> data = new HashMap<>();
        if(getType() == 1)
        {
            System.out.println(ArticleFragment.labels.size());
            for(int i = 3; i < ArticleFragment.labels.size(); i++)
            {
                data.put(i - 3, ArticleFragment.labels.get(i));
            }
        }
        else if(getType() == 0)
        {
            for(int i = 3; i < SuiJiFragment.labels.size(); i++)
            {
                data.put(i - 3, SuiJiFragment.labels.get(i));
            }
        }
        return data;
    }

    //初始化
    private void init()
    {
        Button goBack = rootView.findViewById(R.id.goBack);
        goBack.setOnClickListener(this);
        Button ok_add = rootView.findViewById(R.id.ok_add);
        ok_add.setOnClickListener(this);
        RecyclerView recyclerView = new RecyclerView(getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        ChoseLabelsAdapter choseLabelsAdapter = new ChoseLabelsAdapter();
        Map<Integer, String> data = getData();
        choseLabelsAdapter.setData(data);
        choseLabelsAdapter.setItemCounts(data.size());
        choseLabelsAdapter.setAddLabelFragment(this);
        recyclerView.setAdapter(choseLabelsAdapter);
        recyclerView.setLayoutManager(manager);
        rootView.addView(recyclerView);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.goBack:
            {
                manager.popBackStack();
            }break;
            case R.id.ok_add:
            {
                if(getType() == 0)
                {
                    if(!getChosedLabels().isEmpty())
                    {
                        addSuiJiFragment.setChosedLabel(chosedLabels);
                    }
                    else
                    {
                        addSuiJiFragment.setChosedLabel(new HashMap<>());
                    }
                }
                else if(getType() == 1)
                {
                    if(!getChosedLabels().isEmpty())
                    {
                        addArticleFragment.setChosedLabel(chosedLabels);
                    }
                    else
                    {
                        addArticleFragment.setChosedLabel(new HashMap<>());
                    }

                }
                manager.popBackStack();
            }break;
        }
    }
}
