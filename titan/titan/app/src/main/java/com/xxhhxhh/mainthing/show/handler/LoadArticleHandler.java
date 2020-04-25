package com.xxhhxhh.mainthing.show.handler;

import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import com.xxhhxhh.mainthing.databean.ArticleDataBean;
import com.xxhhxhh.mainthing.index.fragment.OneLabelForArticleFragment;
import com.xxhhxhh.mainthing.show.showarticle.ShowArticleFragment;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoadArticleHandler extends Handler
{
    private int article_id;
    private  ShowArticleFragment showArticleFragment;

    public LoadArticleHandler(int article_id, ShowArticleFragment showArticleFragment)
    {
        this.article_id = article_id;
        this.showArticleFragment = showArticleFragment;
    }

    @Override
    public void handleMessage(@NonNull Message msg)
    {
        if(msg.what == 123)
        {
            getOneArticle();
        }
        else if(msg.what == 456)
        {
            showArticleFragment.init();
        }
    }

    //获取文章内容
    public void getOneArticle()
    {
        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.ONE_ARTICLE);
        //数据
        Map<String, String> data = new HashMap<>();
        data.put("the_label", "");
        data.put("username", "");
        data.put("type", "5");
        data.put("start", "0");
        data.put("article_id", String.valueOf(article_id));


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
                    String result = response.body().string();
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result);
                        if (!jsonObject.get("theUrl").equals("null"))
                        {

                            ArticleDataBean articleDataBean = new ArticleDataBean();
                            articleDataBean.setArticle_id(jsonObject.getInt("the_id"));
                            articleDataBean.setUsername(jsonObject.getString("username"));
                            articleDataBean.setArticle_title(jsonObject.getString("articleTitle"));
                            articleDataBean.setComment_number(jsonObject.getInt("commentNumber"));
                            articleDataBean.setGoods(jsonObject.getInt("goods"));
                            articleDataBean.setNickName(jsonObject.getString("nickName"));
                            articleDataBean.setLocation(jsonObject.getString("location"));
                            articleDataBean.setLooks_number(jsonObject.getInt("looksNumber"));
                            articleDataBean.setThe_date(jsonObject.getString("theDate").substring(0, 10));
                            articleDataBean.setUrl(jsonObject.getString("theUrl"));
                            articleDataBean.setLabels(jsonObject.getString("label").equals("[]") ?
                                    new JSONArray() : new JSONArray(jsonObject.getString("label")));

                            //判断并添加数据
                            if (articleDataBean.getArticle_title().equals("") && articleDataBean.getArticle_title().equals("null"))
                            {
                                articleDataBean.setArticle_title("");
                            }

                            if (articleDataBean.getThe_date().equals("") && articleDataBean.getThe_date().equals("null"))
                            {
                                articleDataBean.setThe_date("");
                            }

                            showArticleFragment.articleDataBean = articleDataBean;
                            response.close();
                            call.cancel();
                            sendEmptyMessage(456);

                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

    }
}
