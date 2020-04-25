package com.xxhhxhh.titanback.user.infoset;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

public class UserRelated extends ActionSupport
{
    //返回
    private int isSuccess;
    public int getIsSuccess() { return isSuccess; }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();

        //类型，0关注、1拉黑,2删除关系
        int type = servletRequest.getParameter("type") != null ?
                Integer.parseInt(servletRequest.getParameter("type")) : 3;

        String username = servletRequest.getParameter("username");

        String toUsername = servletRequest.getParameter("toUsername");

        Session session = GetSessionFactory.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        //按类型设置
        if(type != 3 && username != null && !username.equals("") && toUsername != null && !toUsername.equals(""))
        {
            try
            {
                switch (type)
                {
                    case 0:
                    case 1:
                    {
                        Query query = session.createSQLQuery("insert into Related (type, username, to_username) " +
                                "values( :type , :username , :to_username ) ")
                                .setParameter("type", type)
                                .setParameter("username", username)
                                .setParameter("to_username", toUsername);
                        isSuccess = query.executeUpdate();
                        transaction.commit();
                        Query query1 = session.createSQLQuery("update UserInfo set " +
                                " user_focus = user_focus + 1 where username = :username ")
                                .setParameter("username", username);
                        query1.executeUpdate();
                        transaction.commit();
                        session.close();
                    }break;
                    case 2:
                    {

                        Query query = session.createSQLQuery("delete from Related where username= :username " +
                                " and type= :type ").setParameter("username", username)
                                .setParameter("type", type);
                        isSuccess = query.executeUpdate();
                        transaction.commit();

                    }break;
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        session.close();

        return SUCCESS;
    }
}
