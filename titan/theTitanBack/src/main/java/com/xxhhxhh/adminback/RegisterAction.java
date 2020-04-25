package com.xxhhxhh.adminback;

import com.opensymphony.xwork2.ActionSupport;
import com.xxhhxhh.titanback.util.GetSessionFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class RegisterAction extends ActionSupport
{
    /**此方法用于注册
     * @param username /*用户名
     * @param password /*密码
     * @param repassword /*重新密码
     * @param identifyText /*识别码
     * @reurn 返回主页
     * */
    private String username;
    private String identifyText;
    private String password;
    private String repassword;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest servletRequest = ServletActionContext.getRequest();

        username = servletRequest.getParameter("username") != null ?
                servletRequest.getParameter("username") : "";
        password = servletRequest.getParameter("password") != null ?
                servletRequest.getParameter("password") : "";
        repassword = servletRequest.getParameter("repassword") != null ?
                servletRequest.getParameter("repassword") : "";
        identifyText = servletRequest.getParameter("identifyText") != null ?
                servletRequest.getParameter("identifyText") : "";

        if(!username.equals("") && !password.equals("") && !repassword.equals("") && !identifyText.equals(""))
        {
            //判断存在
            Session session = GetSessionFactory.sessionFactory.openSession();
            List resultList = session.createSQLQuery("select username from AdminUserInfo" +
                    " where username = :username ").setMaxResults(1).setParameter("username", username)
                    .getResultList();
            //
            if(resultList == null || resultList.size() <= 0)
            {
                if(password.equals(repassword))
                {
                    Transaction transaction = session.beginTransaction();
                    Query query = session.createSQLQuery("update AdminUserInfo set username = :username " +
                            " , password = :password where identifyText = :identifyText")
                            .setParameter("username", username).setParameter("password", password)
                            .setParameter("identifyText", identifyText);
                    int result = query.executeUpdate();
                    transaction.commit();
                    if(result >= 1)
                    {
                        return SUCCESS;
                    }
                }
            }

        }

        return ERROR;
    }
}
