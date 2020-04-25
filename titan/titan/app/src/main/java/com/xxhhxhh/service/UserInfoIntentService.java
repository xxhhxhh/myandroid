package com.xxhhxhh.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import androidx.annotation.Nullable;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.MainActivity;
import com.xxhhxhh.mainthing.locoldatabase.SQLiteDataBaseOpenHelper;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.GetUserHeadUtil;
import com.xxhhxhh.transdata.util.HttpsUtil;
import com.xxhhxhh.whenstart.UserLogin;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UserInfoIntentService extends IntentService
{

    public UserInfoIntentService()
    {
        super("username");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        UserInfoApplocation userInfoApplocation = (UserInfoApplocation) getApplication();
        UserLogin.IS_LOGIN = true;
        userInfoApplocation.setUsername(intent.getStringExtra("username"));
        userInfoApplocation.setLoginType(intent.getIntExtra("type", 2));
        HttpsUtil getUserInfo = new HttpsUtil(TheUrls.USER_SETTING);
        Map<String, String> data = new HashMap<>();
        data.put("username", userInfoApplocation.getUsername());
        try
        {
            getUserInfo.setSendData(data);
            getUserInfo.sendPost();
            String ss = getUserInfo.getResponse().body().string();
            JSONObject jsonObject = new JSONObject(ss);
            userInfoApplocation.setAllowDetail(jsonObject.getInt("allowDetail"));
            userInfoApplocation.setAllowLookArticle((Integer) jsonObject.get("allowLookArticle"));
            userInfoApplocation.setAllowLookSuiJi(jsonObject.getInt("allowLookSuiJi"));
            userInfoApplocation.setAllowRecommend(jsonObject.getInt("allowRecommend"));
            userInfoApplocation.setStarngeLogin(jsonObject.getInt("strangeLogin"));
            userInfoApplocation.setNickName(jsonObject.getString("nickName"));
            userInfoApplocation.setIdNumber(jsonObject.getString("idNumber"));
            userInfoApplocation.setRealName(jsonObject.getString("realName"));
            userInfoApplocation.setMyLove(jsonObject.getString("myLove"));
            userInfoApplocation.setMyOther(jsonObject.getString("myOther"));
            getUserInfo.getResponse().close();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        //获取头像
        try
        {
           Bitmap bitmap =  GetUserHeadUtil.getHead(userInfoApplocation.getUsername());
            SharedPreferences preferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
           if(bitmap != null)
           {
               ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
               bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
               String data1 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);
               editor.putString("userHead", data1);
               editor.putString("username", userInfoApplocation.getUsername());
               editor.apply();
           }
           else
           {
                editor.putString("userHead", "");
                editor.apply();
           }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
