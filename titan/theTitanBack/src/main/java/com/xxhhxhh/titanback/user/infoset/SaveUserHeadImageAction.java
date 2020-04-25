package com.xxhhxhh.titanback.user.infoset;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.user.UserInfo;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.util.Base64;

public class SaveUserHeadImageAction extends ActionSupport
{
    private String input;
    //返回
    private int isSuccess;
    public int getIsSuccess() { return isSuccess; }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        input = servletRequest.getParameter("head") != null ?
                servletRequest.getParameter("head") : "";
        String username = servletRequest.getParameter("username") != null?
                servletRequest.getParameter("username") : "";
        byte[] bytes =  Base64.getDecoder().decode(input);


        if(input != null && !input.equals("") && username != null && !username.equals(""))
        {
            Session session = GetSessionFactory.sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Blob blob = Hibernate.getLobCreator(session).createBlob(bytes);
            Query query = session.createQuery("update UserInfo set user_head_image = :head " +
                    " where username = :username").setParameter("username", username)
                    .setParameter("head", blob);
            isSuccess = query.executeUpdate();
            transaction.commit();
            session.close();
        }

        return SUCCESS;
    }
}
