package com.xxhhxhh.mainthing.index.handler;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;
import androidx.fragment.app.Fragment;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.index.adpter.SuiJiAdapter;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.index.dataload.SuiJiDataLoad;
import com.xxhhxhh.mainthing.index.fragment.OneLabelForSuiJiFragment;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SuiJiLoadHandler extends Handler
    implements Runnable
{
    private SuiJiAdapter suiJiAdapter;
    public int start;//起始位置
    public int loadNumber;//加载数量
    public int getStart() { return start; }
    public int getLoadNumber() { return loadNumber; }
    public void setLoadNumber(int loadNumber) { this.loadNumber = loadNumber; }
    public void setStart(int start) { this.start = start; }
    private  int i = 0;
    public void setI(int i) { this.i = i; }
    private SuiJiDataBean suiJiDataBean;
    private OneLabelForSuiJiFragment fragment;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public SuiJiLoadHandler(SuiJiAdapter suiJiAdapter, Fragment fragment)
    {
        this.suiJiAdapter = suiJiAdapter;
        this.fragment = (OneLabelForSuiJiFragment) fragment;
    }


    @Override
    public void run()
    {
        if(i <= getLoadNumber())
        {
            executorService.execute(new SuiJiDataLoad(suiJiAdapter, fragment.username, 1, getStart() + i));
            suiJiAdapter.notifyItemInserted(getStart() + i);
            suiJiAdapter.notifyDataSetChanged();
            i++;
        }
        else
        {
            i = 0;
        }
    }

}
