package com.xxhhxhh.mainthing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.titan.R;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.index.adpter.SuiJiAdapter;
import com.xxhhxhh.mainthing.index.fragment.OneLabelForSuiJiFragment;
import com.xxhhxhh.mainthing.show.showsuiji.ShowSuiJiFragment;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.GetUserHeadUtil;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

//显示随记，随记文件将采用流传输并判断其文件头
public class ShowSuiJiActivity extends FragmentActivity
{

    private FragmentManager manager;
    public ShowSuiJiFragment showSuiJiFragment;
    public int nowItem;//当前标识
    public void setNowItem(int nowItem){this.nowItem = nowItem;}
    public int getNowItem() { return nowItem; }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        showSuiJiFragment = new ShowSuiJiFragment();
        transaction.add(R.id.showMain, showSuiJiFragment, "showSuiJi").addToBackStack("showsuiji").commit();
        setContentView(R.layout.show_main);
    }

    @Override
    public void onBackPressed()
    {
        switch (getNowItem())
        {
            case 0:
            {
                this.finish();
            }break;
            case 1:
            {
                manager.popBackStack();
            }break;
        }
    }




}
