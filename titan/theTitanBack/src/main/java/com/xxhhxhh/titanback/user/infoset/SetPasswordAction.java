package com.xxhhxhh.titanback.user.infoset;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import com.xxhhxhh.titanback.util.UserInfoDoUtil;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

public class SetPasswordAction extends ActionSupport
{
    //返回数据
    private int isSuccess;
    public int getIsSuccess() { return isSuccess; }

    @Override
    public String execute() throws Exception
    {
        String username = "";
        String password = "";
        Session session = GetSessionFactory.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        password = servletRequest.getParameter("password");
        username = servletRequest.getParameter("username");

        //用户名存在则修改
        if(UserInfoDoUtil.findUsername(username, session))
        {
            Query query = session.createSQLQuery("update UserInfo set password='"+ password + "'" +
                    "where username='" + username + "'");

            isSuccess = query.executeUpdate();
            transaction.commit();
            session.close();
        }

        return SUCCESS;
    }
}
