package com.xxhhxhh.mainthing.index.dataload;

import com.xxhhxhh.mainthing.databean.ArticleDataBean;
import com.xxhhxhh.mainthing.index.adpter.ArticleAdapter;
import com.xxhhxhh.mainthing.index.adpter.SuiJiAdapter;
import com.xxhhxhh.mainthing.index.fragment.OneLabelForArticleFragment;
import com.xxhhxhh.mainthing.mine.adapter.ArticleShowAdapter;
import com.xxhhxhh.transdata.urls.TheUrls;
import com.xxhhxhh.transdata.util.HttpsUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ArticleDataLoad implements Runnable
{
    /**加载随记数据
     * @param suiJiAdapter /*适配器
     * @param username /*用户名
     * @param number /*运行次数
     * @param start /*开始位置
     * */
    private int number;
    private String username;
    private ArticleAdapter articleAdapter;
    private int start;

    public ArticleDataLoad(ArticleAdapter articleAdapter, String username, int number, int start)
    {
        this.articleAdapter = articleAdapter;
        this.number = number;
        this.username = username;
        this.start = start;
    }

    @Override
    public void run()
    {
        for(int i = start; i < number + start; i++)
        {
            getOneArticle(i);
        }
    }

    //获取文章内容
    public void getOneArticle(int i)
    {
        HttpsUtil httpsUtil = new HttpsUtil(TheUrls.ONE_ARTICLE);
        //数据
        Map<String, String> data = new HashMap<>();
        data.put("the_label", OneLabelForArticleFragment.label_item != null ? OneLabelForArticleFragment.label_item : "");
        data.put("username", username != null ?
               username : "");
        data.put("start", String.valueOf(i));

        if (OneLabelForArticleFragment.label_item.equals("推荐"))
        {
            data.put("type", "0");
        }
        else if (OneLabelForArticleFragment.label_item.equals("热门"))
        {
            data.put("type", "1");
        }
        else if (OneLabelForArticleFragment.label_item.equals("最新"))
        {
            data.put("type", "2");
        }
        //按标签获取
        else
        {
            data.put("type", "3");
        }

        httpsUtil.setSendData(data);
        //发送
        httpsUtil.sendPost();

        try
        {
            String result = httpsUtil.getResponse().body().string();
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

                httpsUtil.getResponse().close();
                articleAdapter.dataBeans.put(i, articleDataBean);
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

    }
}
