package com.xxhhxhh.mainthing;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.titan.R;
import com.xxhhxhh.mainthing.Factory.PermissionRequestAndCheckFactory;
import com.xxhhxhh.mainthing.addmessage.fragment.AddSuiJiFragment;
import com.xxhhxhh.mainthing.databean.SuiJiDataBeanSimple;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddSuiJiActivity extends FragmentActivity
{
    public int nowItem;
    public int getNowItem() { return nowItem; }
    public void setNowItem(int nowItem) { this.nowItem = nowItem; }
    private AddSuiJiFragment addSuiJiFragment;
    private String[] requestPermissions =
            {
                    Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE
            };
    //修改模式内容
    private SuiJiDataBeanSimple suiJiDataBeanSimple;
    public SuiJiDataBeanSimple getSuiJiDataBeanSimple() { return suiJiDataBeanSimple; }
    private Map<Integer, Bitmap> bitmapMap = new HashMap<>();
    public Map<Integer, Bitmap> getBitmapMap() { return bitmapMap; }
    public void setBitmapMap(Map<Integer, Bitmap> bitmapMap) { this.bitmapMap = bitmapMap; }
    public ExecutorService executorService = Executors.newSingleThreadExecutor();
    private int type;
    public int getType() { return type; }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(!PermissionRequestAndCheckFactory.checkPermission(requestPermissions, this))
        {
            PermissionRequestAndCheckFactory.requestPermission(requestPermissions, this);
        }
        type = getIntent().getIntExtra("type", 0);
        //数据加载
        if(type == 1)
        {
            suiJiDataBeanSimple = (SuiJiDataBeanSimple) getIntent().getSerializableExtra("data");
            //获取文件
            if(suiJiDataBeanSimple != null && suiJiDataBeanSimple.getHave_file() > 0)
            {
                for(int i = 0; i < suiJiDataBeanSimple.getHave_file(); i++)
                {
                    int finalI = i;
                    executorService.execute(()->getFile(finalI, suiJiDataBeanSimple.getSuiji_id()));
                }
            }

        }
        //页面切换
        setContentView(R.layout.activity_add_message);
        addSuiJiFragment = new AddSuiJiFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("addsuiji");
        transaction.add(R.id.addMessagePage, addSuiJiFragment, "addSuiJi");
        transaction.show(addSuiJiFragment);
        transaction.commit();
    }


    @Override
    public void onBackPressed()
    {
        if (getNowItem() == 0) {
            this.finish();
        } else if (getNowItem() == 1) {
            addSuiJiFragment.setAddList(new HashMap<>());
        }
        super.onBackPressed();
    }


    //获取文件数据
    private void getFile(int i, int suiji_id)
    {
        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.ONE_MEDIA);
        Map<String, String> data = new HashMap<>();
        data.put("group_id", String.valueOf(i));
        data.put("suiji_id", String.valueOf(suiji_id));

        httpsUtil.setSendData(data);

        try
        {
            httpsUtil.sendPost();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        try
        {
            String result = httpsUtil.getResponse().body().string();
            JSONObject jsonObject = new JSONObject(result);
            System.out.println(result);
            String file = jsonObject.getString("result");
            byte[] bytes = Base64.decode(file, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            bitmapMap.put(i, BitmapFactory.decodeStream(byteArrayInputStream));

            httpsUtil.getResponse().close();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

}
