package com.xxhhxhh.titanback.article;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.article.ArticleReply;
import com.xxhhxhh.titanback.util.CommentAndReplyDo;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import com.xxhhxhh.titanback.util.UserInfoDoUtil;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class GetArticleReply extends ActionSupport
{

    //需求数据
    private int article_comment_id;//随记评论id
    //返回数据
    private JSONObject jsonObject = new JSONObject();
    private JSONObject finallyResult = new JSONObject();
    public String getFinallyResult() { return finallyResult.toString().replaceAll("\\\\", ""); }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        article_comment_id = servletRequest.getParameter("article_comment_id") != null ?
                Integer.parseInt(servletRequest.getParameter("article_comment_id")) : 0;

        if(article_comment_id != 0)
        {
            Session session = GetSessionFactory.sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            List<Object> result = CommentAndReplyDo.getReply(session, article_comment_id, "article_comment_id",
                    "ArticleReply", ArticleReply.class, "reply_eyi", 3);

            //生成数据
            int i = 0;
            for(Object iterator : result)
            {
                ArticleReply articleReply = (ArticleReply)iterator;
                jsonObject.put("article_comment_id", articleReply.getArticle_comment_id().getArticle_comment_id());
                jsonObject.put("reply_message", articleReply.getReply_message());
                jsonObject.put("username", articleReply.getReply_username().getUsername());
                jsonObject.put("nickName", UserInfoDoUtil.getNickName(articleReply.getReply_username().getUsername(), session));
                jsonObject.put("to_username", articleReply.getTo_username());
                jsonObject.put("to_nickName", UserInfoDoUtil.getNickName(articleReply.getReply_username().getUsername(), session));
                jsonObject.put("id", articleReply.getReply_id());
                finallyResult.put(String.valueOf(i), jsonObject);
                jsonObject = new JSONObject();
                i++;
            }

            session.close();
        }
        return SUCCESS;
    }
}
