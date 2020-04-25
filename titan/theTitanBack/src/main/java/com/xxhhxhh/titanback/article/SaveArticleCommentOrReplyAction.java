package com.xxhhxhh.titanback.article;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.article.ArticleComments;
import com.xxhhxhh.database.article.ArticleReply;
import com.xxhhxhh.database.article.Articles;
import com.xxhhxhh.database.user.UserInfo;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

public class SaveArticleCommentOrReplyAction extends ActionSupport
{
    //返回数据
    private int isSuccess;
    public int getIsSuccess() { return isSuccess; }

    @Override
    public String execute() throws Exception
    {
        Session session = GetSessionFactory.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        //评论id
        int id = servletRequest.getParameter("id") != null ?
                Integer.parseInt(servletRequest.getParameter("id")) : 0;
        //0评论、1回复
        int type = servletRequest.getParameter("type") != null ?
                Integer.parseInt(servletRequest.getParameter("type")) : 2;
        String username = servletRequest.getParameter("username");
        String message = servletRequest.getParameter("message");


        switch (type)
        {
            case 0:
            {
                ArticleComments articleComments = new ArticleComments();
                if(username != null && !username.equals(""))
                {
                    Query query = session.createQuery("from UserInfo where username=:username", UserInfo.class)
                            .setParameter("username", username).setMaxResults(1);
                    if(query.getResultList() != null && query.getResultList().size() > 0)
                    {
                        //发表用户
                        UserInfo userInfo = (UserInfo) query.getResultList().get(0);
                        if(userInfo.getUser_eyi() < 5)
                        {
                            Query query1 = session.createQuery("from Articles where article_id=:article_id", Articles.class)
                                    .setParameter("article_id", id).setMaxResults(1);
                            if(query1.getResultList() != null && query1.getResultList().size() > 0)
                            {
                                //文章id
                                Articles articles = (Articles) query1.getResultList().get(0);
                                articleComments.setComment_message(message);
                                articleComments.setEyi_level(userInfo.getUser_eyi());
                                articleComments.setArticle_id(articles);
                                articleComments.setComment_username(userInfo);
                                session.save(articleComments);
                            }
                            //保存数量
                            Query query2 = session.createSQLQuery("update Articles set " +
                                    " comment_number = comment_number + 1 where article_id = :id ")
                                    .setParameter("id", id);
                            query2.executeUpdate();
                            transaction.commit();
                            session.close();
                        }
                    }
                }
            }break;
            case 1:
            {
                ArticleReply articleReply = new ArticleReply();
                if(username != null && !username.equals(""))
                {
                    Query query = session.createQuery("from UserInfo where username=:username", UserInfo.class)
                            .setParameter("username", username).setMaxResults(1);
                    if(query.getResultList() != null && query.getResultList().size() > 0)
                    {
                        //发表用户
                        UserInfo userInfo = (UserInfo) query.getResultList().get(0);
                        if(userInfo.getUser_eyi() < 5)
                        {
                            Query query1 = session.createQuery("from ArticleComments where article_comment_id=:article_comment_id", ArticleComments.class)
                                    .setParameter("article_comment_id", id).setMaxResults(1);
                            if(query1.getResultList() != null && query1.getResultList().size() > 0)
                            {
                                //评论id
                                ArticleComments articlesComment = (ArticleComments) query1.getResultList().get(0);
                                articleReply.setArticle_comment_id(articlesComment);
                                articleReply.setReply_eyi(userInfo.getUser_eyi());
                                articleReply.setReply_username(userInfo);
                                articleReply.setReply_message(message);
                                articleReply.setTo_username(articlesComment.getComment_username().getUsername());
                                session.save(articleReply);
                            }
                            //保存数量
                            Query query2 = session.createSQLQuery("update ArticleComments set " +
                                    " reply_number = reply_number + 1 where article_comment_id = :id ")
                                    .setParameter("id", id);
                            query2.executeUpdate();
                            transaction.commit();
                            session.close();
                        }
                    }
                }
            }break;
        }

        isSuccess = 1;
        return SUCCESS;
    }


}
