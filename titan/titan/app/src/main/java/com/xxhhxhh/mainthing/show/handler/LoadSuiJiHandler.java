package com.xxhhxhh.mainthing.show.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import androidx.annotation.NonNull;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiDataBeanParceable;
import com.xxhhxhh.mainthing.show.showsuiji.ShowSuiJiFragment;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.GetUserHeadUtil;
import com.xxhhxhh.transdata.util.HttpsUtil;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class LoadSuiJiHandler extends Handler
{

    private int suiji_id;
    private ShowSuiJiFragment showSuiJiFragment;

    public LoadSuiJiHandler(int suiji_id, ShowSuiJiFragment showSuiJiFragment)
    {
        this.suiji_id = suiji_id;
        this.showSuiJiFragment = showSuiJiFragment;
    }

    @Override
    public void handleMessage(@NonNull Message msg)
    {
        if(msg.what == 123)
        {
            getData(suiji_id);
        }
        else if(msg.what == 456)
        {
            showSuiJiFragment.isFocus = true;
            if(showSuiJiFragment.username == null)
            {
                showSuiJiFragment.username = "";
            }
            else if(!showSuiJiFragment.username.equals(""))
            {
                showSuiJiFragment.getFocus();
            }

            showSuiJiFragment.saveLook();
            showSuiJiFragment.saveLabelLook();
            showSuiJiFragment.init();
            showSuiJiFragment.getComments();
        }
    }

    //获取数据
    public void getData(int suiji_id)
    {
        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.ONE_SUIJI_MAIN);
        //数据
        Map<String, String> data = new HashMap<>();
        data.put("the_label", "");
        data.put("username", "");
        data.put("start", "0");
        data.put("type", "5");
        data.put("suiji_id", String.valueOf(suiji_id));

        httpsUtil.setSendData(data);


        try
        {
            Call call = httpsUtil.sendEnquene();
            if(call != null)
            call.enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e)
                {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    String result = response.body().string();
                    try
                    {
                        saveData(result, httpsUtil);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        response.close();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveData(String result, HttpsUtil httpsUtil) throws Exception
    {
        JSONObject jsonObject = new JSONObject(result);
        //数据获取
        if (!jsonObject.get("mainMessage").equals("null"))
        {
            SuiJiDataBeanParceable suiJiDataBean = new SuiJiDataBeanParceable(new SuiJiDataBean());
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

            showSuiJiFragment.suiJiDataBean = suiJiDataBean;

            if(suiJiDataBean.getHave_file() > 0 && suiJiDataBean.getFileType() == 0)
            {
                for(int j = 0; j < suiJiDataBean.getHave_file(); j++)
                {
                    getFile(j, suiJiDataBean.getSuiji_id(), suiJiDataBean, httpsUtil);
                }
            }
            System.out.println(showSuiJiFragment.suiJiDataBean.getMain_message());
            sendEmptyMessage(456);
        }
    }

    private void getFile(int i, int suiji_id, SuiJiDataBeanParceable suiJiDataBean, HttpsUtil httpsUtil)
    {
        httpsUtil.setUrl(TheUrls.ONE_MEDIA);
        Map<String, String> data = new HashMap<>();
        data.put("group_id", String.valueOf(i));
        data.put("suiji_id", String.valueOf(suiji_id));

        httpsUtil.setSendData(data);

        try
        {
            Call call = httpsUtil.sendEnquene();
            call.enqueue(new okhttp3.Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    try
                    {
                        String result = response.body().string();
                        saveFile(i, result, suiJiDataBean);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

    }

    private void saveFile(int i, String result, SuiJiDataBeanParceable suiJiDataBean) throws Exception
    {
        JSONObject jsonObject = new JSONObject(result);

        String file = jsonObject.getString("result");
        byte[] bytes = Base64.decode(file, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        suiJiDataBean.getBitmapMap().put(i, BitmapFactory.decodeStream(byteArrayInputStream));
    }

    //获取头像
    private void getHead(String username, SuiJiDataBeanParceable suiJiDataBean)
    {
        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.GET_HEAD);

        Map<String, String> data = new HashMap<>();
        data.put("username", username);

        httpsUtil.setSendData(data);
        Call call = httpsUtil.sendEnquene();

        try
        {
            if(call != null)
            {
                call.enqueue(new okhttp3.Callback()
                {
                    @Override
                    public void onFailure(Call call, IOException e)
                    {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException
                    {
                        try
                        {
                            InputStream inputStream = response.body().byteStream();
                            byte[] bytes = new byte[inputStream.available()];
                            inputStream.read(bytes);

                            String data1 = Base64.encodeToString(bytes, Base64.NO_WRAP);
                            if(data1 != null)
                            {
                                suiJiDataBean.setUserHead(data1);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
    }

}
