package com.xxhhxhh.transdata.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.xxhhxhh.transdata.urls.TheUrls;
import okhttp3.FormBody;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GetUserHeadUtil
{
    /**此方法用于获取用户头像
     * @param username /*用户名
     *
     * */
    public static Bitmap getHead(String username)
    {
        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.GET_HEAD);

        Map<String, String> data = new HashMap<>();
        data.put("username", username);

        httpsUtil.setSendData(data);
        httpsUtil.sendPost();

        try
        {
            InputStream inputStream = httpsUtil.getResponse().body().byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            httpsUtil.getResponse().close();
            return bitmap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }


    /**此方法用于存储用户头像
     * @param username /*用户名
     * @param data1 /*要存入头像
     * */
    public static void saveHead(String username, String data1)
    {

        Map<String, String> data = new HashMap<>();
        data.put("head", data1);
        data.put("username", username);

        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.SAVE_HEAD);
        httpsUtil.setSendData(data);

        try
        {
            httpsUtil.sendPost();
            httpsUtil.getResponse().close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
