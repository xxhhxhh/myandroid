package com.xxhhxhh.mainthing.datasend;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Map;

public class SuiJiData {

    public static Map<String, Object> suijiData;

    //获取整体
    public static Map<String, Object> getSuijiData() {
        return suijiData;
    }

    //获取图片
    public static List<Bitmap> getPhotos()
    {
        return (List<Bitmap>) suijiData.get("photos");
    }

    //获取文本
    public static String getSuiJiText()
    {
        return (String) suijiData.get("text");
    }

    //赞数
    public static int good()
    {
        return 0;
    }

    //评论数
    public static int commentNumber()
    {
        return 0;
    }

    //
}
