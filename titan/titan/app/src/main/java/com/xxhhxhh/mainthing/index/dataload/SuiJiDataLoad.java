package com.xxhhxhh.mainthing.index.dataload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.xxhhxhh.applicationclass.UserInfoApplocation;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.datasend.SuiJiData;
import com.xxhhxhh.mainthing.index.adpter.SuiJiAdapter;
import com.xxhhxhh.mainthing.index.fragment.OneLabelForSuiJiFragment;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.GetUserHeadUtil;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SuiJiDataLoad implements Runnable
{
    /**加载随记数据
     * @param suiJiAdapter /*适配器
     * @param username /*用户名
     * @param number /*运行次数
     * */
    private int number;
    private String username;
    private SuiJiAdapter suiJiAdapter;
    public int start;

    public SuiJiDataLoad(SuiJiAdapter suiJiAdapter, String username, int number, int start)
    {
        this.suiJiAdapter = suiJiAdapter;
        this.number = number;
        this.username = username;
        this.start = start;
    }


    @Override
    public void run()
    {
        for(int i =  start; i< number + start; i++)
        {
            getData(i);
        }
    }

    //获取数据
    public void getData(int i)
    {
        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.ONE_SUIJI_MAIN);
        //数据
        Map<String, String> data = new HashMap<>();
        data.put("the_label", OneLabelForSuiJiFragment.label_item != null ? OneLabelForSuiJiFragment.label_item : "");
        data.put("username", username != null ? username : "");
        data.put("start", String.valueOf(i));

        if (OneLabelForSuiJiFragment.label_item.equals("推荐"))
        {
            data.put("type", "0");
        }
        else if (OneLabelForSuiJiFragment.label_item.equals("热门"))
        {
            data.put("type", "1");
        }
        else if (OneLabelForSuiJiFragment.label_item.equals("最新"))
        {
            data.put("type", "2");
        }
        //按标签获取
        else {
            data.put("type", "3");
        }

        httpsUtil.setSendData(data);
        //发送
        httpsUtil.sendPost();

        try
        {
            String result = httpsUtil.getResponse().body().string();
            JSONObject jsonObject = new JSONObject(result);
            //数据获取
            if (!jsonObject.get("mainMessage").equals("null"))
            {
                SuiJiDataBean suiJiDataBean = new SuiJiDataBean();
                suiJiDataBean.setBitmapMap(new HashMap<>());
                suiJiDataBean.setComment_number(jsonObject.getInt("commentNumber"));
                suiJiDataBean.setGoods(jsonObject.getInt("goods"));
                suiJiDataBean.setHave_file(jsonObject.getInt("haveFile"));
                suiJiDataBean.setMain_message(jsonObject.getString("mainMessage"));
                suiJiDataBean.setLocation(jsonObject.getString("location"));
                suiJiDataBean.setThe_date(jsonObject.getString("the_date"));
                suiJiDataBean.setSuiji_id(jsonObject.getInt("suiji_id"));
                suiJiDataBean.setLooks_number(jsonObject.getInt("looks_number"));
                suiJiDataBean.setUsername(jsonObject.getString("username"));
                suiJiDataBean.setNickName(jsonObject.getString("nickName"));
                suiJiDataBean.setLabels(jsonObject.getString("labels").equals("[]") ?
                        (new JSONArray()).toString() : new JSONArray(jsonObject.getString("labels")).toString());
                suiJiDataBean.setFileType(jsonObject.getInt("fileType"));

                if (suiJiDataBean.getUsername().equals("") || suiJiDataBean.getUsername().equals("null"))
                {
                    suiJiDataBean.setUsername("");
                }

                getHead(suiJiDataBean.getUsername(), suiJiDataBean);

                if (suiJiDataBean.getThe_date().equals("") && suiJiDataBean.getThe_date().equals("null"))
                {
                    suiJiDataBean.setThe_date("");
                }


                if (suiJiDataBean.getMain_message().equals("") && suiJiDataBean.getMain_message().equals("null"))
                {
                    suiJiDataBean.setMain_message("");
                }

                httpsUtil.getResponse().close();


                if(suiJiDataBean.getHave_file() > 0 && suiJiDataBean.getFileType() == 0)
                {
                    for(int j = 0; j < suiJiDataBean.getHave_file(); j++)
                    {
                        getFile(j, suiJiDataBean.getSuiji_id(), suiJiDataBean);
                    }
                }

                suiJiAdapter.getDataBeans().put(i, suiJiDataBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFile(int i, int suiji_id, SuiJiDataBean suiJiDataBean)
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

            String file = jsonObject.getString("result");
            byte[] bytes = Base64.decode(file, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            suiJiDataBean.getBitmapMap().put(i, BitmapFactory.decodeStream(byteArrayInputStream));

            httpsUtil.getResponse().close();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

    //获取头像
    private void getHead(String username, SuiJiDataBean suiJiDataBean)
    {
       Bitmap bitmap =  GetUserHeadUtil.getHead(username);
       if(bitmap != null)
       {
           ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
           bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
           String data1 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);
           suiJiDataBean.setUserHead(data1);
       }
    }
}
