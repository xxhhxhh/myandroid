package com.xxhhxhh.adminback;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class LoginAction extends ActionSupport
{
    /**此action用于登录
     * @param username /*登录用户名
     * @param password /*登录密码
     * @return  返回管理主页
     * */
    private String username;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();

        username = servletRequest.getParameter("username") != null ?
        servletRequest.getParameter("username") : "";
        String password = servletRequest.getParameter("password") != null ?
                servletRequest.getParameter("password") : "";

        if(!username.equals("") && !password.equals(""))
        {
            Session session = GetSessionFactory.sessionFactory.openSession();
            Query query = session.createSQLQuery("select username from AdminUserInfo " +
                    " where username = '" + username + "' and password = '" + password + "'");

            List result = query.getResultList();

            if(result != null && result.size() > 0)
            {
                setUsername(username);
                session.close();
                return SUCCESS;
            }
        }

        return ERROR;
    }
}
