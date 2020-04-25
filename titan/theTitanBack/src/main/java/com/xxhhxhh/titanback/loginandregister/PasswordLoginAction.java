package com.xxhhxhh.titanback.loginandregister;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.interfaces.ValidLogin;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import com.xxhhxhh.titanback.util.UserInfoDoUtil;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class PasswordLoginAction extends ActionSupport
    implements ValidLogin
{
    private String username;
    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }
    private String password;
    public void setPassword(String password) { this.password = password; }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();
        setUsername(servletRequest.getParameter("username"));
        setPassword(servletRequest.getParameter("password"));

        //验证成功
        if(validLogin())
        {
            return SUCCESS;
        }

        setUsername(null);
        return ERROR;
    }

    @Override
    public boolean validLogin()
    {
        Session session = GetSessionFactory.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        //用户名存在
        if(UserInfoDoUtil.findUsername(username, session))
        {
            //验证密码
            String password = UserInfoDoUtil.findPassword(username, session);
            if(password != null && password.equals(this.password))
            {
                UserInfoDoUtil.setLoginDate(username, session);
                session.close();
                return true;
            }
        }
        session.close();
        return false;
    }

}
