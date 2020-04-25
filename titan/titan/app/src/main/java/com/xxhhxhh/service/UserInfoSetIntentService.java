package com.xxhhxhh.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.Nullable;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import com.xxhhxhh.transdata.util.THESEARCH;

import java.util.HashMap;
import java.util.Map;

public class UserInfoSetIntentService extends IntentService
{

    public UserInfoSetIntentService()
    {
        super("userInfoSet");
    }

    //更改用户设置
    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        UserInfoApplocation userInfoApplocation = (UserInfoApplocation)getApplication();
        HttpsUtil httpsUtil;
        Map<String, String> data = new HashMap<>();
        try
        {
            //修改用户状态信息
            httpsUtil = new HttpsUtil(TheUrls.SWITCH_SETTING);
            data.put("username", userInfoApplocation.getUsername());
            data.put("allowDetail", String.valueOf(userInfoApplocation.getAllowDetail()));
            data.put("allowLookArticle", String.valueOf(userInfoApplocation.getAllowLookArticle()));
            data.put("allowLookSuiJi", String.valueOf(userInfoApplocation.getAllowLookSuiJi()));
            data.put("allowRecommend", String.valueOf(userInfoApplocation.getAllowRecommend()));
            data.put("strangeLogin", String.valueOf(userInfoApplocation.getStarngeLogin()));
            httpsUtil.setSendData(data);
            httpsUtil.sendPost();
        }
        catch (Exception e)
        {
            Log.d("异常", e.toString());
        }

        data = new HashMap<>();

        //非法信息检测
        THESEARCH thesearch = new THESEARCH();
        thesearch.setMode(1);
        thesearch.setName(userInfoApplocation.getMyLove());
        String mylove = thesearch.toStart();
        thesearch.setName(userInfoApplocation.getMyOther());
        String myOther = thesearch.toStart();

        try
        {
            //修改用户自定义信息
            httpsUtil = new HttpsUtil(TheUrls.CUSTOM_SETTING);
            data.put("username", userInfoApplocation.getUsername());
            if(mylove.equals("no"))
            {
                data.put("myLove", "");
            }
            else
            {
                data.put("myLove", mylove);
            }
            if(myOther.equals(""))
            {
                data.put("myOther", "");
            }
            else
            {
                data.put("myOther", myOther);
            }
            data.put("nickName", userInfoApplocation.getNickName());
            httpsUtil.setSendData(data);
            httpsUtil.sendPost();
            httpsUtil.getResponse().close();
        }
        catch (Exception e)
        {
            Log.d("异常", e.toString());
        }

    }
}
