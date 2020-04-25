package com.xxhhxhh.titanback.user.infoset;


import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

public class SetUserIdentifyAction extends ActionSupport
{
    //返回数据
    private int isSuccess;
    public int getIsSuccess() { return isSuccess; }

    @Override
    public String execute() throws Exception
    {
        String username = "";
        String id_number = "";
        String real_name = "";
        Session session = GetSessionFactory.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        real_name = servletRequest.getParameter("real_name");
        username = servletRequest.getParameter("username");
        id_number = servletRequest.getParameter("id_number");
        Query query = session.createSQLQuery("update UserInfo set real_name=:real_name, id_number=:id_number " +
                "where username=:username").setParameter("username", username).setParameter("real_name", real_name)
                .setParameter("id_number", id_number);

        isSuccess = query.executeUpdate();
        transaction.commit();
        session.close();
        return SUCCESS;
    }
}
