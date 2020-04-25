package com.xxhhxhh.transdata.util;

import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommentAndReplyAndResetDoUtil
{
    /** 此方法用于评论和回复的存储
    * @param commentType  /*评论类型，0评论、1回复
     * @param data       /*post数据
     * @param theType   /*0代表随记，1代表文章
    * **/
    public static void SaveCommentOrReply(int commentType, Map<String, String> data, int theType)
    {
        HttpsUtil httpsUtil = null;

        //设置url
        switch (theType)
        {
            case 0:
            {
                httpsUtil = new HttpsUtil(TheUrls.SAVE_SUIJI_COMMENT_OR_REPLY);
            }break;
            case 1:
            {
                httpsUtil = new HttpsUtil(TheUrls.SAVE_ARTICLE_COMMENT_OR_REPLY);
            }break;
        }

        //设置模式
        switch (commentType)
        {
            //评论
            case 0:
            {
                data.put("type", "0");
            }break;
            //回复
            case 1:
            {
                data.put("type", "1");
            }break;
        }

        if(httpsUtil != null)
        {
            httpsUtil.setSendData(data);

            try
            {
                httpsUtil.sendPost();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try
            {
                httpsUtil.getResponse().close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**此方法用于评论、回复获取
     * @param id /*要获取内容的id
     * @param type /*类型：0随记，1文章,2随记回复，3文章回复
     * */
    public static Map<Integer, JSONObject> getComment(int id, int type)
    {
        Map<String, String> data = new HashMap<>();
        HttpsUtil httpsUtil = null;
        switch (type)
        {
            case 0:
            {
                data.put("suiji_id", String.valueOf(id));

                httpsUtil = new HttpsUtil(TheUrls.GET_SUIJI_COMMENT);


            }break;
            case 1:
            {
                data.put("article_id", String.valueOf(id));

                httpsUtil = new HttpsUtil(TheUrls.GET_ARTICLE_COMMENT);

            }break;
            case 2:
            {
                data.put("comment_id", String.valueOf(id));

                httpsUtil = new HttpsUtil(TheUrls.GET_SUIJI_REPLY);
            }break;
            case 3:
            {
                data.put("comment_id", String.valueOf(id));

                httpsUtil = new HttpsUtil(TheUrls.GET_ARTICLE_REPLY);
            }break;
        }
        //结果
        Map<Integer, JSONObject> map = new HashMap<>();
        if(httpsUtil != null)
        {
            try
            {
                httpsUtil.setSendData(data);
                httpsUtil.sendPost();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try
            {


                String result = httpsUtil.getResponse().body().string();
                System.out.println(result);
                JSONObject jsonObject1 = new JSONObject(result);
                JSONObject jsonObject = new JSONObject(jsonObject1.getString("finallyResult"));
                if(!jsonObject.toString().equals(""))
                {
                    int i = 0;
                    JSONObject jsonObject2;
                    for (Iterator<String> it = jsonObject.keys(); it.hasNext(); )
                    {
                        String key = it.next();
                        jsonObject2 = new JSONObject(jsonObject.getString(key));
                        map.put(i, jsonObject2);
                        i++;
                    }
                    return map;
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
                return map;
            }
            finally
            {
                return map;
            }
        }
        return map;
    }

}
