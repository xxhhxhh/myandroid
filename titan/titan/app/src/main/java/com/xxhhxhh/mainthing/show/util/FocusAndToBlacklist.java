package com.xxhhxhh.mainthing.show.util;

import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FocusAndToBlacklist
{
    /** 此方法用于插入用户关系
     * @param type /*类型，0关注，1拉黑,2删除
     * @param username /*关系用户
     * @param toUserName /*被关系用户
     * */
    public static void doFocusAndBlacklist(int type, String username, String toUserName)
    {
        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.SAVE_USER_RELATED);

        Map<String, String> data = new HashMap<>();
        data.put("type", "" + type);
        data.put("username", username);
        data.put("toUsername", toUserName);

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

    /** 此方法用于获取用户关系
     * @param type /*0关注，1拉黑
     * @param username /*关系用户
     * @param searchType /*0全部查找，1查询单个
     * @return 返回字典， key:被关系用户 value:关系用户(该值重复)
     * */

    public static Map<String, String> getUserRelated(int type, String username, int searchType, String toUsername)
    {
        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.GET_USER_RELATED);

        Map<String, String> data = new HashMap<>();
        data.put("type", String.valueOf(type));
        data.put("username", username != null ? username : "");
        data.put("searchType", String.valueOf(searchType));
        data.put("toUsername", toUsername);
        httpsUtil.setSendData(data);
        httpsUtil.sendPost();


        try
        {
            String result = httpsUtil.getResponse().body().string();
            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            String result1 = jsonObject.getString("toUsernames");
            JSONArray jsonArray = new JSONArray(result1);
            Map<String, String> resultData = new HashMap<>();
            for(int i = 0; i < jsonArray.length(); i++)
            {
                resultData.put(jsonArray.getString(i), username);
            }

            return resultData;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new HashMap<>();
    }
}
