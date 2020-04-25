package com.xxhhxhh.mainthing.show.util;

import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;

import java.util.HashMap;
import java.util.Map;

public class GiveGood
{
    /**此方法收集点赞信息
     * @param id /*id标识
     * @param type /*0随记、1文章、2随记评论、3文章评论
     * */
    public static void giveGood(int id, int type)
    {
        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.GIVE_GOOD);
        Map<String, String> data = new HashMap<>();
        data.put("id", String.valueOf(id));
        data.put("type", String.valueOf(type));

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
