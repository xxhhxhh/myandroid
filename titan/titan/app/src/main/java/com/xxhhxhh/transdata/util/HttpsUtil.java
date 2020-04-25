package com.xxhhxhh.transdata.util;

import okhttp3.*;

import java.net.URL;
import java.util.Map;

public class HttpsUtil
{
    //对象
    private OkHttpClient okHttpClient;
    public OkHttpClient getOkHttpClient() { return okHttpClient; }
    public void setOkHttpClient(OkHttpClient okHttpClient) { this.okHttpClient = okHttpClient; }

    //返回内容
    private Response response;
    public Response getResponse() { return response; }
    public void setResponse(Response response) { this.response = response; }
    private Map<String, String> sendData;
    public void setSendData(Map<String, String> sendData) { this.sendData = sendData; }
    public Map<String, String> getSendData() { return sendData != null && sendData.isEmpty()?null:sendData; }
    private String url;
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    //构造方法
    public HttpsUtil(String url)
    {
        setOkHttpClient(new OkHttpClient());
        setResponse(null);
        setSendData(null);
        setUrl(url);
    }

    //post内容构建
    public FormBody makePostBody() throws Exception
    {
        FormBody.Builder postBuilder = new FormBody.Builder();
        for(String key : getSendData().keySet())
        {
            postBuilder.add(key, getSendData().get(key));
        }

        return postBuilder.build();
    }

    //发送get请求
    public boolean sendGet()
    {
        setUrl(getUrl() + "?");
        for(String key : getSendData().keySet())
        {
            setUrl(getUrl() + key + "=" + getSendData().get(key) + "&");
        }

        try
        {
            Request request = new Request.Builder().url(getUrl()).build();
            Call call = okHttpClient.newCall(request);
            setResponse(call.execute());
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    //发送post请求
    public boolean sendPost()
    {
        try
        {
            Request request = new Request.Builder().url(getUrl()).post(makePostBody()).build();
            Call call = okHttpClient.newCall(request);
            setResponse(call.execute());
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            //发送失败
            return false;
        }
        //发送成功
        return true;
    }

    //异步请求
    public Call sendEnquene()
    {
        try
        {
            Request request = new Request.Builder().url(getUrl()).post(makePostBody()).build();
            Call call = okHttpClient.newCall(request);
            return call;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
