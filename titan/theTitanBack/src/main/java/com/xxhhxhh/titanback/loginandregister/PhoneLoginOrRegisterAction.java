package com.xxhhxhh.titanback.loginandregister;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.database.user.UserInfo;
import com.xxhhxhh.interfaces.ValidLogin;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import com.xxhhxhh.titanback.util.UserInfoDoUtil;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;

public class PhoneLoginOrRegisterAction extends ActionSupport
    implements ValidLogin
{
    private String username;
    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }
    private int type;
    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest httpServletRequest = ServletActionContext.getRequest();
        setUsername(httpServletRequest.getParameter("username"));
        if(validLogin())
        {
            return SUCCESS;
        }
        else
        {
            setUsername("");
            return ERROR;
        }
    }

    @Override
    public boolean validLogin()
    {
        Session session = GetSessionFactory.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        //不存在用户名
        if(!UserInfoDoUtil.findUsername(username, session) && username != null)
        {
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(username);
            Timestamp date = new Timestamp(System.currentTimeMillis());
            userInfo.setLogin_date(date);
            userInfo.setRegister_date(date);
            session.save(userInfo);
            transaction.commit();
            //检验注册是否成功
            if(UserInfoDoUtil.findUsername(username, session))
            {
                setType(1);
                session.close();
                return true;
            }
            else
            {
                setType(2);
                session.close();
                return false;
            }
        }

        UserInfoDoUtil.setLoginDate(username, session);
        session.close();
        setType(0);
        return true;
    }
}
