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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.xxhhxhh.mainthing.AddSuiJiActivity;
import com.example.titan.R;
import com.xxhhxhh.mainthing.addmessage.adapter.ChosePeopleAdapter;

import java.util.HashMap;
import java.util.Map;

public class AddPeopleFragment extends Fragment
        implements View.OnClickListener
{
    //选中标签
    public String chosedPeople;

    //管理
    private FragmentManager manager;
    private AddSuiJiFragment addSuiJiFragment;
    private LinearLayout rootView;//根视图
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        manager = getActivity().getSupportFragmentManager();
        //初始化模式
        addSuiJiFragment = (AddSuiJiFragment) manager.findFragmentByTag("addSuiJi");
        ((AddSuiJiActivity)getActivity()).setNowItem(4);
        rootView = (LinearLayout) inflater.inflate(R.layout.choseing_page, null);
        init();
        return rootView;
    }

    //获取标签数据
    private Map<Integer, String> getData()
    {
        Map<Integer, String> data = new HashMap<>();
        data.put(0, "1");

        data.put(1, "2");
        data.put(2, "2");

        return data;
    }

    //初始化
    private void init()
    {
        Button goBack = rootView.findViewById(R.id.goBack);
        goBack.setOnClickListener(this);
        Button ok_add = rootView.findViewById(R.id.ok_add);
        ok_add.setVisibility(View.INVISIBLE);
        //视图配置
        RecyclerView recyclerView = new RecyclerView(getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        ChosePeopleAdapter chosePeopleAdapter = new ChosePeopleAdapter();
        chosePeopleAdapter.setAddSuiJiFragment(addSuiJiFragment);
        Map<Integer, String> data = getData();
        chosePeopleAdapter.setItemCounts(data.size());
        chosePeopleAdapter.setData(data);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(chosePeopleAdapter);
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
        }
    }
}
