package com.xxhhxhh.mainthing;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.titan.R;
import com.xxhhxhh.mainthing.Factory.PermissionRequestAndCheckFactory;
import com.xxhhxhh.mainthing.addmessage.fragment.AddArticleFragment;
import com.xxhhxhh.mainthing.databean.ArticleDataBean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddArticleActivity extends FragmentActivity {

    public int nowItem;
    public int getNowItem() { return nowItem; }
    public void setNowItem(int nowItem) { this.nowItem = nowItem; }
    private AddArticleFragment addArticleFragment;
    private String[] requestPermissions =
            {
                    Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE
            };
    //修改模式
    private ArticleDataBean articleDataBean;
    public ArticleDataBean getArticleDataBean() { return articleDataBean; }
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
        if(type == 1)
        {
            articleDataBean = (ArticleDataBean) getIntent().getSerializableExtra("data");
        }

        setContentView(R.layout.activity_add_message);
        //页面切换
        addArticleFragment = new AddArticleFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("addarticle");
        transaction.add(R.id.addMessagePage, addArticleFragment, "addArticle");
        transaction.show(addArticleFragment);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed()
    {
        if(getNowItem() == 0)
        {
            this.finish();
        }
        else if(getNowItem() == 1)
        {
            addArticleFragment.setAddList(new HashMap<>());
        }
        super.onBackPressed();
    }

}
