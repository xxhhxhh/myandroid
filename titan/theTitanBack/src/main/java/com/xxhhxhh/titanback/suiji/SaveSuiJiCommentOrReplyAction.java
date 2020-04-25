package com.xxhhxhh.titanback.suiji;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.suiji.SuiJiComments;
import com.xxhhxhh.database.suiji.SuiJiReply;
import com.xxhhxhh.database.suiji.SuiJis;
import com.xxhhxhh.database.user.UserInfo;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

public class SaveSuiJiCommentOrReplyAction extends ActionSupport
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

        if(type != 3 && message != null && !message.equals("") && id != 0) {
            switch (type) {
                //评论
                case 0: {
                    SuiJiComments suiJiComments = new SuiJiComments();
                    if (username != null && !username.equals("")) {
                        Query query = session.createQuery("from UserInfo where username=:username", UserInfo.class)
                                .setParameter("username", username).setMaxResults(1);
                        if (query.getResultList() != null && query.getResultList().size() > 0) {
                            //发表用户
                            UserInfo userInfo = (UserInfo) query.getResultList().get(0);
                            if (userInfo.getUser_eyi() < 20)
                            {
                                Query query1 = session.createQuery("from SuiJis where suiji_id=:suiji_id", SuiJis.class)
                                        .setParameter("suiji_id", id).setMaxResults(1);
                                if (query1.getResultList() != null && query1.getResultList().size() > 0)
                                {
                                    //文章id
                                    SuiJis suiJis = (SuiJis) query1.getResultList().get(0);
                                    suiJiComments.setComment_message(message);
                                    suiJiComments.setEyi_level(userInfo.getUser_eyi());
                                    suiJiComments.setSuiji_id(suiJis);
                                    suiJiComments.setComment_username(userInfo);
                                    session.save(suiJiComments);

                                }
                                Query query2 = session.createSQLQuery("update SuiJis set comment_number" +
                                        " = comment_number + 1 where suiji_id= :suiji_id ")
                                        .setParameter("suiji_id", id);
                                query2.executeUpdate();
                                transaction.commit();
                                session.close();
                            }
                        }
                    }
                }
                break;
                //回复
                case 1: {
                    SuiJiReply suiJiReply = new SuiJiReply();
                    if (username != null && !username.equals("")) {
                        Query query = session.createQuery("from UserInfo where username=:username", UserInfo.class)
                                .setParameter("username", username).setMaxResults(1);
                        if (query.getResultList() != null && query.getResultList().size() > 0) {
                            //发表用户
                            UserInfo userInfo = (UserInfo) query.getResultList().get(0);
                            if (userInfo.getUser_eyi() < 5)
                            {
                                Query query1 = session.createQuery("from SuiJiComments where suiji_comment_id=:suiji_comment_id", SuiJiComments.class)
                                        .setParameter("suiji_comment_id", id).setMaxResults(1);
                                if (query1.getResultList() != null && query1.getResultList().size() > 0) {
                                    //评论id
                                    SuiJiComments suiJiComments = (SuiJiComments) query1.getResultList().get(0);
                                    suiJiReply.setSuiji_comment_id(suiJiComments);
                                    suiJiReply.setReply_eyi(userInfo.getUser_eyi());
                                    suiJiReply.setReply_username(userInfo);
                                    suiJiReply.setReply_message(message);
                                    suiJiReply.setTo_username(suiJiComments.getComment_username().getUsername());
                                    session.save(suiJiReply);
                                }
                                //保存数量
                                Query query2 = session.createSQLQuery("update SuiJiComments set " +
                                        " reply_number = reply_number + 1 where suiji_comment_id= :id ")
                                        .setParameter("id", id);
                                query2.executeUpdate();
                                transaction.commit();
                                session.close();
                            }
                        }
                    }
                }
                break;
            }
        }

        isSuccess = 1;
        return SUCCESS;
    }
}
