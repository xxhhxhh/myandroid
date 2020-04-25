package com.xxhhxhh.mainthing.show.commentsandreply.loaddata;

import android.graphics.Bitmap;
import android.util.Base64;
import com.xxhhxhh.mainthing.databean.ArticleCommentDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiCommentDataBean;
import com.xxhhxhh.mainthing.databean.SuiJiDataBean;
import com.xxhhxhh.mainthing.show.commentsandreply.adapter.ShowCommentsAdapter;
import com.xxhhxhh.transdata.util.CommentAndReplyAndResetDoUtil;
import com.xxhhxhh.transdata.util.GetUserHeadUtil;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class ArticleCommentLoad implements Runnable
{
    private ShowCommentsAdapter showCommentsAdapter;
    private int article_id;

    public ArticleCommentLoad(ShowCommentsAdapter showCommentsAdapter, int  article_id)
    {
        this.showCommentsAdapter = showCommentsAdapter;
        this.article_id= article_id;
    }

    @Override
    public void run()
    {
        Map<Integer, JSONObject> data = CommentAndReplyAndResetDoUtil.getComment( article_id, 1);

        for(int i = 0; i < data.size(); i++)
        {
            try
            {
                ArticleCommentDataBean articleCommentDataBean = new ArticleCommentDataBean();
                articleCommentDataBean.setComment_message(data.get(i).getString("commentMessage"));
                articleCommentDataBean.setComment_username(data.get(i).getString("username"));
                articleCommentDataBean.setNickName(data.get(i).getString("nickName"));
                articleCommentDataBean.setGoods(data.get(i).getInt("goods"));
                articleCommentDataBean.setReply_number(data.get(i).getInt("replyNumber"));
                articleCommentDataBean.setArticle_comment_id(data.get(i).getInt("article_comment_id"));
                getHead(articleCommentDataBean.getComment_username(), articleCommentDataBean);
                showCommentsAdapter.getArticleCommentDataBeanMap().put(i, articleCommentDataBean);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    //获取头像
    private void getHead(String username, ArticleCommentDataBean articleCommentDataBean)
    {
        Bitmap bitmap =  GetUserHeadUtil.getHead(username);
        if(bitmap != null)
        {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
            String data1 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);
            articleCommentDataBean.setUserHead(data1);
        }
    }
}
