package com.xxhhxhh.titanback.article;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.article.ArticleComments;
import com.xxhhxhh.titanback.util.CommentAndReplyDo;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import com.xxhhxhh.titanback.util.UserInfoDoUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class GetArticleCommentAction extends ActionSupport
{
    //需求数据
    private int article_id;
    public static final String tableName = "ArticleComments";
    public static final String id_name = "article_id";
    //返回数据
    private JSONObject resultData = new JSONObject();
    private JSONObject finallyResult = new JSONObject();
    public String getfinallyResult(){return finallyResult.toString().replaceAll("\\\\", "");}


    @Override
    public String execute() throws Exception
    {
        HttpServletRequest request = ServletActionContext.getRequest();
        Session session = GetSessionFactory.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        article_id = request.getParameter("article_id") != null ?
        Integer.parseInt(request.getParameter("article_id")) : 0;
        List<Object> result = CommentAndReplyDo.backComment(session,
                article_id, id_name, tableName, ArticleComments.class, "eyi_level");
        //迭代对象
        int i =0;
        for(Object iterator : result)
        {
            ArticleComments articleComments = (ArticleComments) iterator;
            resultData.put("goods", articleComments.getGoods());
            resultData.put("commentMessage", articleComments.getComment_message());
            resultData.put("replyNumber", articleComments.getReply_number());
            resultData.put("username", articleComments.getComment_username().getUsername());
            resultData.put("nickName", UserInfoDoUtil.getNickName(articleComments.getComment_username().getUsername(), session));
            resultData.put("article_comment_id", articleComments.getArticle_comment_id());
            finallyResult.put(String.valueOf(i), resultData);
            resultData = new JSONObject();
            i++;
        }

        session.close();

        return SUCCESS;
    }


}
