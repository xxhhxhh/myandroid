package com.xxhhxhh.titanback.user.infoget;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class GetUserHeadImageAction extends ActionSupport
{
    private InputStream inputStream;
    public InputStream getInputStream() { return inputStream; }
    public void setInputStream(InputStream inputStream) { this.inputStream = inputStream; }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        String username = servletRequest.getParameter("username") != null ?
                servletRequest.getParameter("username") : "";

        if(username != null && !username.equals(""))
        {
            Session session = GetSessionFactory.sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            Query query = session.createSQLQuery("select user_head_image from UserInfo " +
                    " where username= :username ").setParameter("username", username).setMaxResults(1);

            List result = query.getResultList();
            if(result != null && result.size() > 0)
            {
                byte[] bytes = (byte[]) result.get(0);
                if(bytes != null)
                {
                    setInputStream(new ByteArrayInputStream(bytes));
                }
                else
                {
                    setInputStream(null);
                }
            }

            session.close();
        }


        return SUCCESS;
    }
}
